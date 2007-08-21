package node;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class DownloadManager {
	
	private NodeUI nodeUI;
	private String filename;
	private static final int packetLength=512 * 1024;
	private long offSet;
	private long filesize;
	
	public DownloadManager(NodeUI n, String filename) {
		this.nodeUI = n;
		this.filename = filename;
	}
	
	public void download() {
		try {
			INode n = nodeUI.connectNode(nodeUI.getMachines().get(0));
			filesize = n.getFileSize(filename);
			offSet = 0;
			for(String s: nodeUI.getMachines()) {
				INode ns = nodeUI.connectNode(s);
				new Download(ns, offSet).start();
				offSet += packetLength;
				if (offSet > filesize ) break;
			}
			
		} catch(Exception e) {
			System.err.println("Erro no download: " + e.getMessage());
		}
	}
	
	
	void completed(INode ns, long offset, boolean ok) {
		if (offset < filesize) {
			new Download(ns, offSet).start();
			offSet += packetLength;
		} else
			this.nodeUI.makeFile(this.filename);
	}
	
	class Download extends Thread {
		INode node;
		long offset;
		
		Download(INode n, long offset) {
			this.node = n;
			this.offset = offset;
		}

		public void run() {
			
			try {
				nodeUI.downloadFile(filename, offset, packetLength, node);
				completed(node, offset, true);
			} catch (RemoteException e) {
				completed(node, offset, false);
			}
		}
		
		
		
	}


}
