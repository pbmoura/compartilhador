package nameserver;

import java.util.*;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class NameServer extends UnicastRemoteObject implements INameServer {
	private String name;
	private Vector supernodes;
	private Random rand;
	public NameServer (String name) throws RemoteException {
		super();
		this.name = name;
		supernodes = new Vector();
		rand = new Random();
	}
	
	//Retorna o Endereço de um super no qualquer para cadastro
	public String getSuperNode() throws RemoteException {
		int r = Math.abs(rand.nextInt()) % supernodes.size();
		return (String) supernodes.get(r);
	}
	
	public void init() {
		String objname = "//"+name+"/nameserver";
		try {
			Naming.rebind(objname, this);
			System.err.println("Servidor de nomes pronto...");
		} catch(Exception e) {
			System.err.println("Erro inicializando servidor: " + e.getMessage());
		}
		
	}
	
	//Retorna um vetor com todos os supernos
	public Vector getSuperNodes() throws RemoteException{
		if (!supernodes.isEmpty()) {
			return supernodes;
		} 
		return null;
	}
	//Cadastro de super no
	public void addSuperNode(String address) throws RemoteException{
		supernodes.add(address);
		System.out.println(supernodes);
	}

	public static void main(String[] args) throws Exception {
		System.err.println("Entrando...");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
	    } 
		NameServer s = new NameServer(args[0]);
		s.init();
	}

	/* (non-Javadoc)
	 * @see nameserver.INameServer#disconnect(java.lang.String)
	 */
	public void disconnect(String name) throws RemoteException {
		supernodes.remove(name);
		
	}
}