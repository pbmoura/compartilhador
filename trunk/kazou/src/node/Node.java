/*
 * Created on 18/06/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package node;

import java.rmi.RemoteException;

/**
 * @author rodrigo
 *
 * Esta classe representa um cliente da rede
 */
public class Node {
	private String name;
	NodeServer ns;
	NodeUI nui;
	public Node(String name,String nameserver, String repository) {
		this.name = name;
		try {
			ns = new NodeServer(name, repository);
			nui = new NodeUI(name,nameserver, repository);
			//new Thread(ns).start();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		new Thread(nui).start();		
	}
	
	public static void main(String[] args) {
		new Node(args[0],args[1], args[2]);
	}
}
