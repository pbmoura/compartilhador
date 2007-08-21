/*
 * Created on 18/06/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package supernode;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.JOptionPane;

import node.INodeUI;
import node.Node;


/**
 * @author rodrigo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SuperNode extends UnicastRemoteObject implements ISuperNode {
	private String name;
	private Hashtable files;
	private ISuperNode sNode;
	private List superNodes;
	private Node n;
	private Vector runningSearchs;
	private Random rand;
	public SuperNode (String name, String superNode, String repository) throws RemoteException {
		super();
		this.name = name;
		files = new Hashtable();
		runningSearchs = new Vector();
		superNodes = new ArrayList();
		init(superNode);
		n = new Node(name,superNode, repository);
		rand = new Random();
	}
		
	private void init(String superNode) {
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
		try {
			String objname = "//"+this.name+"/supernode";
			if (!this.name.equals(superNode)) {
				String supNode = "//"+superNode+"/supernode";
	    		sNode = (ISuperNode) Naming.lookup(supNode);
	    		System.err.println("Entrando...");
	    		sNode.addSuperNode(this.name);
	    		this.addSuperNode(superNode);
			}
			Naming.rebind(objname,this);
    		System.err.println("SuperNode pronto...");
    		this.addSuperNode(name);
    	} catch (java.rmi.ConnectException ce) {
    		JOptionPane.showMessageDialog(null,"Não foi possivel conectar a //"+superNode);
    		ce.printStackTrace();
 
    	} catch(Exception e) {
    		JOptionPane.showMessageDialog(null,"Erro desconhecido na conexão com o servidor");
    		e.printStackTrace();
    	}
    	
	}
	
	public void addSuperNode(String name) {
		superNodes.add(name);
	}
	
	public String getRadomSuperNode() {
		int r = Math.abs(rand.nextInt()) % superNodes.size();
		return (String) superNodes.get(r);
	}
	
	/** 
	 * Retorna uma lista com todos os supernos
	 */
	public List getSuperNodes() throws RemoteException{
		if (!superNodes.isEmpty()) {
			return superNodes;
		} 
		return null;
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
	public void setNode(String nodeAddress, List filevector) throws RemoteException {
		List nodes;
		for (int i=0;i<filevector.size();i++) {
			if (files.containsKey(filevector.get(i))) {
				nodes = (List) files.get(filevector.get(i));
			} else {
				nodes = new ArrayList();
				files.put(filevector.get(i),nodes);
			}
			nodes.add(nodeAddress);
		}
	}

	private INodeUI getNodeUI(String name) {
		String Name = "//"+name+"/nodeui";
		try {
			return (INodeUI) Naming.lookup(Name);
		} catch (Exception e) {
			System.err.println("Erro recuperando NodeUI: " + e.getMessage());
		}
		
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see supernode.ISuperNode#searchFile(java.lang.String)
	 */
	public List searchFile(String file, String name) throws RemoteException {
		//se a busca já foi feita neste supernó
		if (runningSearchs.contains(name))
			return null;
		
		runningSearchs.add(name);
		
		//Vector v = nameserver.getSuperNodes();
		List v = getSuperNodes();
		List machines = new ArrayList();
		
		if ((List)files.get(file)!= null) {
			//machines.addAll((Vector)nodes.get(file));
			getNodeUI(name).addMachines((List)files.get(file));
		}
		for (int i=0;i < v.size();i++) {
			String address = (String)v.get(i);
			if (!address.equalsIgnoreCase(this.name)) {
				System.out.println("Estou procurando em :"+address);
				ISuperNode sn = connectToSuperNode(address);
				if (sn!=null){
					System.out.println("SN não eh nulo: "+address);
					sn.searchFile(file, name);
					/*List c = sn.getFileNodes(file);
					if (c!=null) {
						//machines.addAll(c);
						getNodeUI(name).addMachines((List)c);
					}*/
				}
			}
		}
		
		
		runningSearchs.remove(name);
		return machines;
	}
	

	/* (non-Javadoc)
	 * @see supernode.ISuperNode#getVector(java.lang.String)
	 */
	public List getFileNodes(String file) throws RemoteException {
		return (List)files.get(file);
	}

	/* (non-Javadoc)
	 * @see supernode.ISuperNode#disconnect(java.lang.String)
	 */
	public void disconnect(String name) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}
