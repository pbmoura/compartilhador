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
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import supernode.ISuperNode;
import business.FileInfo;

public class NodeUI extends UnicastRemoteObject implements Runnable, INodeUI {
	
	private String ip;
	private ISuperNode superNode;
	private INode node;
	private String superNodeIP;
	private String repository;
	private List<String> machines;
	private List<FileInfo> filesInfos;
	File folder;
	
	public NodeUI(String ip, String superNodeIP, String repository) throws RemoteException {
		super();
		this.ip = ip;
		this.superNodeIP = superNodeIP;
		this.repository = repository;
		this.machines = new ArrayList<String>();
		this.filesInfos = new ArrayList<FileInfo>();
		init();
	}
	
	private void init() {
		
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
	    }
		
		String objname = "//"+this.ip+"/nodeui";
		try {
			Naming.rebind(objname, this);
			System.out.println("interface pronta");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getMachines() {
		return machines;
	}
	
	public List<FileInfo> getFilesInfos() {
		return filesInfos;
	}

	
	public void run() {
		//retorna super no
		System.out.println("NSNAME eh: "+this.superNodeIP);
		connectToSuperNode(this.superNodeIP);
		System.out.println("dependo do superno: "+this.superNodeIP);
		insertClient();
		searchFile();
	}
	
	public synchronized void addMachines(List v) {
		machines.addAll(v);
	}
	
	public synchronized void addFilesInfos(List v) {
		filesInfos.addAll(v);
	}
	
	public void finish() {
		
	}
	
	/**
	 * Insere esse cliente no superNode juntamente
	 * com a lista de arquivos disponíveis nesse cliente.
	 */
	private void insertClient() {
		folder = new File(repository);
		File [] folderList = folder.listFiles();
		
		List<File> folderListAux = new ArrayList<File>();
		
		for (int i=0;i<folderList.length;i++) {
			folderListAux.add(folderList[i]);
		}
		
		try {
			superNode.setNode(this.ip, geraFileHashTable(folderListAux));
		} catch(RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private Hashtable geraFileHashTable(List<File> folderListAux) {
		Hashtable<String, FileInfo> hashMd5 = new Hashtable<String, FileInfo>();
		for (Iterator iterator = folderListAux.iterator(); iterator.hasNext();) {
			File file = (File) iterator.next();
			hashMd5.put(file.getName(), new FileInfo(file));
			
		}
		return hashMd5;
	}
	
	public void disconnect(){
		
	}
	
	private void searchFile() {
		while (true) {
			System.out.println("Digite o nome do arquivo");
			String fileName = IO.readStr();

			if (fileName.equals("") || fileName == null) {
				disconnect();
				System.exit(0);

			}

			filesInfos.clear();
			try {
				superNode.searchFileByName(fileName, ip);

				if (filesInfos != null && !filesInfos.isEmpty()) {
					System.out.println(filesInfos);
					int choice = IO.readInt();
					FileInfo fileInfo = filesInfos.get(choice);
					machines.clear();
					superNode.searchFileByHash(fileInfo.getHashValue(), ip);
					new DownloadManager(this, fileName, fileInfo.getHashValue()).download();

					/*
					 * try { String file = node.getFile(str); writeFile(str,
					 * file); } catch (RemoteException e) { e.printStackTrace(); }
					 */

				} else {
					System.out.println("Arquivo nao encontrado");
				}

			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	
	private void connectToSuperNode(String server) {
		
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
		
		try {
    		String serverName = "//"+server+"/supernode";
    		superNode = (ISuperNode) Naming.lookup(serverName);
    		System.out.println("CONSEGUI");
    	} catch (java.rmi.ConnectException ce) {
    		JOptionPane.showMessageDialog(null,"Não foi possivel conectar a //"+server+"/supernode");
    	} catch(Exception e) {
    		JOptionPane.showMessageDialog(null,"Erro desconhecido na conexão com o servidor");
    		e.printStackTrace();
    	}
    }

	public void downloadFile(String nome, String hash, long startOffset,int totallength, INode ns) throws RemoteException {
		
		// Criar um diretorio para conter os downloads do arquivo
		String diretorio=nome.replace('.','_');
		File dir=new File(repository+File.separator+diretorio);
		
		if(!dir.exists()){
			dir.mkdir();
		}
		
		// Cada pacote tera 1KB de dados
		final int length = 1024;     // 1 KB
		
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
			
			buffer=ns.getFileParts(hash,offset,length);
			
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