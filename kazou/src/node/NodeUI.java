package node;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.JOptionPane;

import nameserver.INameServer;
import supernode.ISuperNode;

public class NodeUI implements Runnable {
	private String name;
	private INameServer nameserver;
	private ISuperNode supernode;
	private INode node;
	private String nsname;
	private String repository;
	File folder;
	
	public NodeUI(String name, String ns, String repository) {
		super();
		this.name = name;
		this.nsname = ns;
		this.repository = repository;
	}
	
	public void run() {
		//retorna super no
		System.out.println("NSNAME eh: "+nsname);
		String sn = connectNameServer(this.nsname);
		connectSuperNode(sn);
		System.out.println("dependo do superno: "+sn);
		insertClient();
		searchFile();
	}
	
	private void insertClient() {
		//File folder = new File("/tmp/kazou");
		folder = new File(repository);
		String [] str = folder.list();
		Vector v = new Vector();
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
			Vector v = getAddressVector(str);
			if (v!=null && !v.isEmpty()) {
				System.out.println(v);
				int choice = IO.readInt();
				connectNode((String)v.get(choice));
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
	
	private Vector getAddressVector (String file) {
		Vector v = null;
		try {
			v = supernode.searchFile(file);
			
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
	
	private String connectNameServer (String s) {
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
		try {
    		String ServerName = "//"+s+"/nameserver";
    		nameserver = (INameServer) Naming.lookup(ServerName);
    		System.out.println("CONSEGUI");
    	} catch (java.rmi.ConnectException ce) {
    		JOptionPane.showMessageDialog(null,"Não foi possivel conectar a //"+s+"/simorg");
    	} catch(Exception e) {
    		//System.err.println("Erro desconhecido.."); e.printStackTrace();
    		JOptionPane.showMessageDialog(null,"Erro desconhecido na conexão com o servidor");
    		e.printStackTrace();
    		//System.exit(1);
    	}
    	String result = "";
    	try {
    		//Vector coisa = nameserver.getSuperNodes();
    		//System.out.println(coisa);
    		result = nameserver.getSuperNode();
    	} catch (RemoteException re) {
    		System.out.println("Não consegui retornar um supernode");
    		re.printStackTrace();
    	}
    	return result;
    }
	
}