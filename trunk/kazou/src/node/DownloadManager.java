package node;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class DownloadManager {
	
	private NodeUI nodeUI;
	private String filename;
	private String hashCode;
	private static final int packetLength = 512 * 1024;
	private long offSet;
	private long filesize;
	
	private LinkedList<Long> missing;
	private Vector<Long> downloading;
	private List<String> machines;
	
	
	public DownloadManager(NodeUI node, String file, String hash) {
		this.nodeUI = node;
		this.filename = file;
		this.hashCode = hash;
	}
	
	public void download() {
		try {
			INode n = nodeUI.connectNode(nodeUI.getMachines().get(0));
			filesize = n.getFileSize(hashCode);
			offSet = 0;
			
			downloading = new Vector<Long>();
			missing = new LinkedList<Long>();
			for(long i = 0; i < filesize; i += packetLength)
				missing.addFirst(i);
			
			machines = nodeUI.getMachines();
			
			for(String s: machines) {
				INode ns = nodeUI.connectNode(s);
				downloading.add(missing.getFirst());
				new Download(ns, missing.removeFirst()).start();
				offSet += packetLength;
				if (missing.isEmpty() ) break;
			}
			
			/*for(String s: nodeUI.getMachines()) {
				INode ns = nodeUI.connectNode(s);
				new Download(ns, offSet).start();
				offSet += packetLength;
				if (offSet > filesize ) break;
			}*/
			
		} catch(Exception e) {
			System.err.println("Erro no download: " + e.getMessage());
		}
	}
	
	
	void completed(INode ns, long offset, boolean ok) {
		downloading.remove(offset);
		if (!ok) {
			missing.addLast(offset);
			machines.remove(ns);
			if (machines.isEmpty())
				System.err.println("nao tem mais ninguem...");
		}
		else
			if (!missing.isEmpty())
				new Download(ns, missing.removeFirst()).start();
			else
				if (downloading.isEmpty())
					this.nodeUI.makeFile(this.filename);
					
		/*if (offset < filesize) {
			new Download(ns, offSet).start();
			offSet += packetLength;
		} else
			this.nodeUI.makeFile(this.filename);*/
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
				nodeUI.downloadFile(filename, hashCode, offset, packetLength, node);
				completed(node, offset, true);
			} catch (RemoteException e) {
				completed(node, offset, false);
			}
		}
		
		
		
	}


}
