/*
 * Created on 18/06/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package supernode;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.JOptionPane;

import node.INodeUI;
import node.Node;
import node.NodeUI;
import util.Connection;
import util.Net;
import business.Controller;
import business.FileInfo;


/**
 * @author rodrigo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SuperNode extends UnicastRemoteObject implements ISuperNode {
	private String name;
	private Hashtable files_hash;
	private Hashtable hash_machines;
	private ISuperNode sNode;
	private List superNodes;
	private Node n;
	private Vector runningSearchs;
	private Random rand;
	private Connection connection;
	
	public SuperNode (String repository) throws RemoteException {
		super();
		try {
			this.name = Net.getLocalIPAddress();

		files_hash = new Hashtable<String, List<FileInfo>>();
		hash_machines = new Hashtable<String, List<String>>();
		runningSearchs = new Vector();
		superNodes = new ArrayList();
		connection = new Connection(this.name);
		init();
		//n = new Node(name, name, repository);
		try {
			n = new Node(name, repository);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rand = new Random();
		} catch (IOException e1) {
			Controller.getInstance().showException(e1.getMessage(),true);
			e1.printStackTrace();
		}
	}
		
	private void init() {
		String superNode = null;
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
		try {
			superNode = connection.connect();
			if (superNode != null) {
				sNode = (ISuperNode)Naming.lookup("//" + superNode + "/supernode");
				sNode.addSuperNode(this.name);
	    		this.addSuperNode(superNode);
			}
			
			/*
			if (!this.name.equals(superNode)) {
				String supNode = "//"+superNode+"/supernode";
	    		sNode = (ISuperNode) Naming.lookup(supNode);
	    		System.err.println("Entrando...");
	    		sNode.addSuperNode(this.name);
	    		this.addSuperNode(superNode);
			}
			*/
			
			
			String objname = "//"+this.name+"/supernode";
			Naming.rebind(objname,this);
    		System.err.println("SuperNode pronto...");
    		//this.addSuperNode(name);
    	} catch (java.rmi.ConnectException ce) {
    		ce.printStackTrace();
    		Controller.getInstance().showException("Não foi possivel conectar a //"+superNode,true);
 
    	} catch(Exception e) {
    		e.printStackTrace();
    		Controller.getInstance().showException("Erro desconhecido na conexão com o servidor",true);
    	}
    	
	}
	
	public String getName() {
		return name;
	}
	
	public void addSuperNode(String name) {
		if(!superNodes.contains(name))
			superNodes.add(name);
	}
	
	public String getRadomSuperNode() {
		int r = Math.abs(rand.nextInt()) % superNodes.size();
		return (String) superNodes.get(r);
	}
	
	/** 
	 * Retorna uma lista com todos os supernos
	 */
	public List getSuperNodes() throws RemoteException {
		if (!superNodes.isEmpty()) {
			return superNodes;
		} 
		return new ArrayList();
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
			new SuperNode(args[0]);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* 
	 * Recebe o endereço do cliente e cadastra seus arquivos em sua tabela Hash
	 */
	public void setNode(String nodeAddress, Hashtable filevector) throws RemoteException {

		for (Iterator iterator = filevector.keySet().iterator(); iterator.hasNext();) {
			String name = (String) iterator.next();
			FileInfo fNode = (FileInfo)filevector.get(name);
			if (files_hash.containsKey(name)) {
				List<FileInfo> fileInfos = (List) files_hash.get(name);
				boolean achou=false;
				for (Iterator iterator2 = fileInfos.iterator(); iterator2
						.hasNext();) {
					FileInfo fInfo = (FileInfo) iterator2.next();
					if (fInfo.getHashValue().equals(fNode.getHashValue())) {
						List<String> machines = (List)hash_machines.get(
								fNode.getHashValue());
						machines.add(nodeAddress);
						achou = true;
						break;
					} 
					
				}
				
				if (!achou) { 
					fileInfos.add(fNode);
					ArrayList al = new ArrayList();
					al.add(nodeAddress);
					hash_machines.put(fNode.getHashValue(), al);
				}
			} else {
				ArrayList al = new ArrayList();
				al.add(fNode);
				files_hash.put(name, al);
				if (hash_machines.keySet().contains(fNode.getHashValue())) {
					List<String> machines = (List)hash_machines.get(fNode.getHashValue());
					if (!machines.contains(nodeAddress)) {
						machines.add(nodeAddress);
					}
				} else {
					ArrayList a = new ArrayList();
					a.add(nodeAddress);
					hash_machines.put(fNode.getHashValue(), a);
				}
			}
			
		}
		
		
		
//		List nodes;
//		
//		for (int i=0;i<filevector.size();i++) {
//			if (files.containsKey(filevector.get(i))) {
//				nodes = (List) files.get(filevector.get(i));
//			} else {
//				nodes = new ArrayList();
//				files.put(filevector.get(i),nodes);
//			}
//			nodes.add(nodeAddress);
//		}
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
	
	public List searchFileByHash(String hash, String name) throws RemoteException{
		if (runningSearchs.contains(name))
			return null;
		
		runningSearchs.add(name);
		
		//Vector v = nameserver.getSuperNodes();
		List v = getSuperNodes();
		List machines = new ArrayList();
		
		if ((List)hash_machines.get(hash)!= null) {
			//machines.addAll((Vector)nodes.get(file));
			getNodeUI(name).addMachines((List)hash_machines.get(hash));
		}
		for (int i=0;i < v.size();i++) {
			String address = (String)v.get(i);
			if (!address.equalsIgnoreCase(this.name)) {
				System.out.println("Estou procurando em :"+address);
				try {
					ISuperNode sn = connectToSuperNode(address);
					if (sn!=null){
						System.out.println("SN não eh nulo: "+address);
						sn.searchFileByHash(hash, name);
					}
				} catch (RemoteException e) {
					System.err.println("nao consegui buscar em " + address);
				}
			}
		}
		
		
		runningSearchs.remove(name);
		return machines;
	}
	
	/* (non-Javadoc)
	 * @see supernode.ISuperNode#searchFile(java.lang.String)
	 */
	public void searchFileByName(String file, String name) throws RemoteException {
		//se a busca já foi feita neste supernó
		if (runningSearchs.contains(name))
			return;
		
		runningSearchs.add(name);
		
		//Vector v = nameserver.getSuperNodes();
		List v = getSuperNodes();
		List machines = new ArrayList();
		
		
		if (files_hash!= null) {
			//machines.addAll((Vector)nodes.get(file));
			//getNodeUI(name).addFilesInfos((List)files_hash.get(file));
			Iterator it= files_hash.keySet().iterator();
			if(it!=null){
				while(it.hasNext()){
					String aux=(String)it.next();
					System.out.println(aux +" "+file);
					if(aux.toLowerCase().contains(file.toLowerCase()))
						getNodeUI(name).addFilesInfos((List)files_hash.get(aux));
					
				}
			}
		}
		
		for (int i=0;i < v.size();i++) {
			String address = (String)v.get(i);
			if (!address.equalsIgnoreCase(this.name)) {
				System.out.println("Estou procurando em :"+address);
				try {
					ISuperNode sn = connectToSuperNode(address);
					if (sn!=null){
						System.out.println("SN não eh nulo: "+address);
						sn.searchFileByName(file, name);
	
					}
				} catch(RemoteException e) {
					System.err.println("nao consegui buscar em " + address);
				}
			}
		}
		
		
		runningSearchs.remove(name);

	}
	

//	/* (non-Javadoc)
//	 * @see supernode.ISuperNode#getVector(java.lang.String)
//	 */
//	public List getFileNodes(String file) throws RemoteException {
//		return null;
//	}

	/* (non-Javadoc)
	 * @see supernode.ISuperNode#disconnect(java.lang.String)
	 */
	public void disconnect(String name) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	public NodeUI getNodeUI() {
		return n.getNodeUI();
	}

}
