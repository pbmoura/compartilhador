/*
 * Created on 18/06/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;



/**
 * @author rodrigo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NodeServer extends UnicastRemoteObject implements INode {
	private String name;
	private String repository; //adicionado por yguaratã
	public NodeServer(String name, String repository) throws RemoteException {
		super();
		this.name = name;
		this.repository = repository;
		init();
	}
	
	public NodeServer() throws RemoteException {
		super();
	}
	
	private void init () {
		System.err.println("Imprimindo name..."+name);
		System.err.println("Carregando servidor de arquivos...");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
	    } 
		//NodeServer s = new NodeServer();
		String objname = "//"+this.name+"/nodeserver";
		try {
			Naming.rebind(objname, this);
			System.err.println("servidor de arquivos pronto");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/* (non-Javadoc)
	 * @see node.INode#getFile(java.lang.String)
	 */
	public String getFile(String file) throws RemoteException {
		System.out.println("getFile");
		String str = "";
		try {
			File path = new File(this.repository + File.separator + file);
			System.out.println(path.toString());
			FileReader filereader = new FileReader(path);
			int tam = (int)path.length();
			char[] buffer = new char[tam];
			filereader.read(buffer,0,tam);
			filereader.close();
			str = new String(buffer);
			return str;
		} catch (FileNotFoundException e) {
			System.err.println("Não foi possível achar o arquivo");
		} catch (IOException ioe) {
			System.err.println("Erro no arquivo");
		}
		
		return str;
	}
}
