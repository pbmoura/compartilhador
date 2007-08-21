/*
 * Created on 18/06/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package node;

import java.io.IOException;
import java.rmi.RemoteException;

import util.Net;

/**
 * @author rodrigo
 *
 * Esta classe representa um cliente da rede
 */
public class Node {
	
	private String ip;
	NodeServer nodeServer;
	NodeUI nodeUI;
	
	public Node(String nameServerIP, String repository) throws IOException {
		this.ip = Net.getLocalIPAddress();
		try {
			nodeServer = new NodeServer(ip, repository);
			nodeUI = new NodeUI(ip, nameServerIP, repository);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		new Thread(nodeUI).start();		
	}
	
	public static void main(String[] args) throws IOException {
		new Node(args[0],args[1]);
	}
}
