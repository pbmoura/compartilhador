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
	
	private String ip;
	NodeServer nodeServer;
	NodeUI nodeUI;
	
	public Node(String ip, String nameServerIP, String repository) {
		this.ip = ip;
		try {
			nodeServer = new NodeServer(ip, repository);
			nodeUI = new NodeUI(ip, nameServerIP, repository);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		new Thread(nodeUI).start();		
	}
	
	public static void main(String[] args) {
		new Node(args[0],args[1], args[2]);
	}
}
