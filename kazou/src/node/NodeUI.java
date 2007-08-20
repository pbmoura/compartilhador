package node;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import supernode.ISuperNode;

public class NodeUI extends UnicastRemoteObject implements Runnable, INodeUI {
	private String name;
	private ISuperNode supernode;
	private INode node;
	//private String nsname;
	private String superNodeName;
	private String repository;
	private List machines;
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
				connectNode((String)machines.get(choice));
				try {
					String file = node.getFile(str);
					writeFile(str, file);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
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
	
	private void connectNode(String server) {
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
		try {
    		String ServerName = "//"+server+"/nodeserver";
    		node = (INode) Naming.lookup(ServerName);
    		System.out.println("CONSEGUI");
    	} catch (java.rmi.ConnectException ce) {
    		JOptionPane.showMessageDialog(null,"Não foi possivel conectar a //"+server+"/node");
    	} catch(Exception e) {
    		JOptionPane.showMessageDialog(null,"Erro desconhecido na conexão com o servidor");
    		e.printStackTrace();
    	}
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
	
}