package node;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import supernode.ISuperNode;

public class NodeUI extends UnicastRemoteObject implements Runnable, INodeUI {
	private String name;
	private ISuperNode supernode;
	private INode node;
	//private String nsname;
	private String superNodeName;
	private String repository;
	private List<String> machines;
	File folder;
	
	public NodeUI(String name, String ns, String repository) throws RemoteException {
		super();
		this.name = name;
		this.superNodeName = ns;
		this.repository = repository;
		this.machines = new ArrayList();
		init();
	}
	
	private void init() {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
	    } 
		//NodeServer s = new NodeServer();
		String objname = "//"+this.name+"/nodeui";
		try {
			Naming.rebind(objname, this);
			System.err.println("interface pronta");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getMachines() {
		return machines;
	}
	
	public void run() {
		//retorna super no
		System.out.println("NSNAME eh: "+this.superNodeName);
		connectSuperNode(this.superNodeName);
		System.out.println("dependo do superno: "+this.superNodeName);
		insertClient();
		searchFile();
	}
	
	public synchronized void addMachines(List v) {
		machines.addAll(v);
	}
	
	public void finish() {
		
	}
	
	private void insertClient() {
		//File folder = new File("/tmp/kazou");
		folder = new File(repository);
		String [] str = folder.list();
		List v = new ArrayList();
		for (int i=0;i<str.length;i++) {
			v.add(str[i]);
		}
		try {
			supernode.setNode(this.name,v);
		} catch(RemoteException e) {
			e.printStackTrace();
		}
		
	}
	public void disconnect(){
		
	}
	private void searchFile () {
		while (true) {
			System.out.println("Digite o nome do arquivo");
			String str = IO.readStr();
			if (str.equals("") || str==null){
				disconnect();
				System.exit(0);
			} 
			machines.clear();
			getAddressVector(str);
			if (machines!=null && !machines.isEmpty()) {
				System.out.println(machines);
				int choice = IO.readInt();
				connectNode(machines.get(choice));
				new DownloadManager(this, str).download();
				
				/*try {
					String file = node.getFile(str);
					writeFile(str, file);
				} catch (RemoteException e) {
					e.printStackTrace();
				}*/
				
			} else {
				System.out.println("Arquivo nao encontrado");
			}
			node = null;
		}
	}
	
	private void writeFile (String filename, String file) {
        try {
            //BufferedWriter out = new BufferedWriter(new FileWriter("/tmp/kazou/"+filename));
        	BufferedWriter out = new BufferedWriter(new FileWriter(folder+File.separator+filename));
            out.write(file);
            out.close();
        } catch (IOException e) {
            System.err.println("Erro ao gravar arquivo");
        }
	}
	
	private List getAddressVector (String file) {
		List v = null;
		try {
			v = supernode.searchFile(file, name);
			
		} catch (RemoteException re) {
			re.printStackTrace();
		}
		return v;
	}
	
	INode connectNode(String server) {
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
		try {
    		String ServerName = "//"+server+"/nodeserver";
    		//node = (INode) Naming.lookup(ServerName);
    		return (INode) Naming.lookup(ServerName);
    		//System.out.println("CONSEGUI");
    	} catch (java.rmi.ConnectException ce) {
    		JOptionPane.showMessageDialog(null,"Não foi possivel conectar a //"+server+"/node");
    	} catch(Exception e) {
    		JOptionPane.showMessageDialog(null,"Erro desconhecido na conexão com o servidor");
    		e.printStackTrace();
    	}
    	return null;
    }
	
	private void connectSuperNode(String server) {
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
		try {
    		String ServerName = "//"+server+"/supernode";
    		supernode = (ISuperNode) Naming.lookup(ServerName);
    		System.out.println("CONSEGUI");
    	} catch (java.rmi.ConnectException ce) {
    		JOptionPane.showMessageDialog(null,"Não foi possivel conectar a //"+server+"/supernode");
    	} catch(Exception e) {
    		//System.err.println("Erro desconhecido.."); e.printStackTrace();
    		JOptionPane.showMessageDialog(null,"Erro desconhecido na conexão com o servidor");
    		e.printStackTrace();
    		//System.exit(1);
    	}
    }

	public void downloadFile(String nome,long startOffset,int totallength, INode ns) throws RemoteException {
		/**
		 * Objeto(s) de coneccao com o server
		 */
		//TODO: apagar proxima linha 
		//MainTest mt=new MainTest();
		//long size=mt.getFileSize(nome);
		
		// Criar um diretorio para conter os downloads do arquivo
		String diretorio=nome.replace('.','_');
		File dir=new File(repository+File.separator+diretorio);
		if(!dir.exists()){
			dir.mkdir();
			//TODO gravar arquivo com o tamnho total do arquivo
		}
		
		// Cada pacote tera 1KB de dados
		final int length =1024;     // 1 KB
		
		long offset=startOffset;
		
		
		// numero de loops
		int counter=totallength/length;
		if(totallength % length>0)
			counter++;
				
		
		// Criando o arquivo
		FileOutputStream fs=null;
		try {
			File f=new File(repository+File.separator+diretorio+File.separator+diretorio+"."+offset);
			//File f=new File(repository+File.separator+2+nome);
			f.createNewFile();
			
			fs=new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		DataOutputStream dos=new DataOutputStream(fs);
		// Escrever parte do arquivo
		
		
		for(int i=0;i<counter;i++){
			
			byte[] buffer;
			
			buffer=ns.getFileParts(nome,offset,length);
			
			if(buffer==null)
				break;
			// Avancar o offset no arquivo
			offset+=length;
			
			try {
				dos.write(buffer);
			} catch (IOException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
		
		try {
			dos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	/**
	 * Quando o download do arquivo esta completo, o metodo tem de ser chamado para fazer com que o arquivo seja
	 * juntado em partes
	 * @param nome
	 */
	public void makeFile(String nome){
		String diretorio=nome.replace('.','_');
		
		File dir=new File(repository+File.separator+diretorio);
		
		String[] files = dir.list();
		
		// Variavel usada para ordenar os arquivos em ordem alfabetica
		long[] alfa=new long[files.length];
		
		for(int i=0;i<files.length;i++){
			
			alfa[i]=Long.parseLong(files[i].substring(files[i].lastIndexOf(".")+1));
				
		}
		
		int[] ord=new int[alfa.length];
		
		long[] beta=alfa.clone();
		
		//ordenando o array
				
		for(int i=0;i<alfa.length;i++){
			long min=Long.MAX_VALUE;
			int index=0;
			
			for(int j=0;j<alfa.length;j++){
				if(alfa[j]<min){
					index=j;
					min=alfa[j];
				}
			}
			
			alfa[index]=Long.MAX_VALUE;
			ord[i]=index;
		}
		
		// Testa se o arquivo esta completo
		int totalSize=0;
		for(int i=0;i<ord.length;i++){
			File f=new File(repository+File.separator+diretorio+File.separator+files[ord[i]]);
			totalSize+=f.length();
			
				
		}
		
		
		//Juntando as partes do arquivo
		
		FileOutputStream fs=null;
		try {
			File f=new File(repository+File.separator+nome);
			f.createNewFile();
			
			fs=new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		DataOutputStream dos=new DataOutputStream(fs);
		
		for(int i=0;i<ord.length;i++){
			
			
			DataInputStream dis=null;
		
			try {
				
				dis=new DataInputStream(new FileInputStream(new File(repository+File.separator+diretorio+File.separator+files[ord[i]])));
				File f=new File(repository+File.separator+diretorio+File.separator+files[ord[i]]);
								
				try {
					byte[] buffer=new byte[(int)f.length()];
					dis.readFully(buffer);
					dos.write(buffer);
										
					dis.close();
					
				
					
				}catch (EOFException e) {
					System.out.println("EOF");
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		try {
			dos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(File file: dir.listFiles()) 
			file.delete();
		dir.delete();
		
	}

}