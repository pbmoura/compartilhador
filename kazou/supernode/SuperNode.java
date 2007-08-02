/*
 * Created on 18/06/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package kazou.supernode;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;

import kazou.nameserver.INameServer;
import kazou.node.Node;


/**
 * @author rodrigo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SuperNode extends UnicastRemoteObject implements ISuperNode {
	private String name;
	private Hashtable nodes;
	private Node n;
	private INameServer nameserver;
	public SuperNode (String name,String nameserveradd, String repository) throws RemoteException {
		super();
		this.name = name;
		nodes = new Hashtable();
		init(nameserveradd);
		n = new Node(name,nameserveradd, repository);
	}
		
	private void init(String nserver) {
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
		try {
    		String ServerName = "//"+nserver+"/nameserver";
    		nameserver = (INameServer) Naming.lookup(ServerName);
    		System.err.println("Entrando...");
    		String objname = "//"+this.name+"/supernode";
    		nameserver.setSuperNode(this.name);
    		Naming.rebind(objname,this);
    		System.err.println("SuperNode pronto...");
    	} catch (java.rmi.ConnectException ce) {
    		JOptionPane.showMessageDialog(null,"Não foi possivel conectar a //"+nserver);
    		ce.printStackTrace();
 
    	} catch(Exception e) {
    		JOptionPane.showMessageDialog(null,"Erro desconhecido na conexão com o servidor");
    		e.printStackTrace();
    	}
    	
	}
	
	private ISuperNode connectToSuperNode (String address) {
		ISuperNode sn = null;
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
		try {
    		String ServerName = "//"+address+"/supernode";
    		sn = (ISuperNode) Naming.lookup(ServerName);
    		System.out.println("CONSEGUI");
    	} catch (java.rmi.ConnectException ce) {
    		JOptionPane.showMessageDialog(null,"Não foi possivel conectar a //"+address+"/simorg");
    	} catch(Exception e) {
    		JOptionPane.showMessageDialog(null,"Erro desconhecido na conexão com o servidor");
    		e.printStackTrace();
    	}
    	return sn;
	}
	
	public static void main(String[] args) {
		try {
			new SuperNode(args[0],args[1], args[2]);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* 
	 * Recebe o endereço do cliente e cadastra seus arquivos em sua tabela Hash
	 */
	public void setNode(String NodeAddress, Vector filevector) throws RemoteException {
		Vector v;
		for (int i=0;i<filevector.size();i++) {
			if (nodes.containsKey(filevector.get(i))) {
				v = (Vector) nodes.get(filevector.get(i));
			} else {
				v = new Vector();
				nodes.put(filevector.get(i),v);
			}
			v.add(NodeAddress);
		}
	}

	/* (non-Javadoc)
	 * @see supernode.ISuperNode#searchFile(java.lang.String)
	 */
	public Vector searchFile(String file) throws RemoteException {
		Vector v = nameserver.getSuperNodes();
		Vector machines = new Vector();
		if ((Vector)nodes.get(file)!=null) {
			machines.addAll((Vector)nodes.get(file));
		}
		for (int i=0;i<v.size();i++) {
			String address = (String)v.get(i);
			if (!address.equalsIgnoreCase(this.name)) {
				System.out.println("Estou procurando em :"+address);
				ISuperNode sn = connectToSuperNode(address);
				if (sn!=null){
					System.out.println("SN não eh nulo: "+address);
					Collection c = sn.getVector(file);
					if (c!=null) {
						machines.addAll(c);
					}
				}
			}
		}
		return machines;
	}
	

	/* (non-Javadoc)
	 * @see supernode.ISuperNode#getVector(java.lang.String)
	 */
	public Vector getVector(String file) throws RemoteException {
		return (Vector)nodes.get(file);
	}

	/* (non-Javadoc)
	 * @see supernode.ISuperNode#disconnect(java.lang.String)
	 */
	public void disconnect(String name) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}
