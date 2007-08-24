package node;

import java.io.File;
import java.io.FileNotFoundException;
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
	
	public String getFileName() {
		return filename;
	}
	
	public void download() throws FileNotFoundException {
		try {
			//for(int i=0;i<)
			List<String> nodes = nodeUI.getMachines();
			for (String node: nodes) {
				INode n = nodeUI.connectNode(node);
				filesize = n.getFileSize(hashCode);
				if (filesize > -1) break;
			}
			//INode n = nodeUI.connectNode(nodeUI.getMachines().get(0));
			//filesize = n.getFileSize(hashCode);
			
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
			//System.err.println("Erro no download: " + e.getMessage());
			throw new FileNotFoundException();
		}
	}
	
	/**
	 * 
	 * 
	 * @param off - deslocamento
	 * @param size - tamanho do bloco que esta faltando
	 * @throws FileNotFoundException
	 */
	private void continueDownload(long off,int size) throws FileNotFoundException {
		try {
			offSet = off;
			
			downloading = new Vector<Long>();
			missing = new LinkedList<Long>();
			for(long i = 0; i <size; i += packetLength)
				missing.addFirst(i);
			
			machines = nodeUI.getMachines();
			
			for(String s: machines) {
				INode ns = nodeUI.connectNode(s);
				downloading.add(missing.getFirst());
				new Download(ns, missing.removeFirst()).start();
				offSet += packetLength;
				if (missing.isEmpty() ) break;
			}
			
		
			
		} catch(Exception e) {
			//System.err.println("Erro no download: " + e.getMessage());
			throw new FileNotFoundException();
		}
	}
	
	synchronized void completed(INode ns, long offset, boolean ok) {
		downloading.remove(offset);
		if (!ok) {
			missing.addLast(offset);
			machines.remove(ns);
			if (machines.isEmpty())
				System.err.println("nao tem mais ninguem...");
		}
		else {
			if (!missing.isEmpty()) {
				downloading.add(missing.getFirst());
				new Download(ns, missing.removeFirst()).start();
			} else {
				if (downloading.isEmpty()){
					
					System.out.println("chamando o makefile");
					Vector<Long> v=this.nodeUI.makeFile(this.filename);
					if(v==null){
						nodeUI.downloadFinished(this);
						System.out.println("sucesso");
					}else if(v.size()==0){
						
					}else {
							long off=v.get(0);
							long end=v.get(1);
							int siz=(int)(end-off);
							v.remove(0);
							v.remove(1);
							try {
								this.continueDownload(off, siz);
							} catch (FileNotFoundException e) {
								
								e.printStackTrace();
							}
						
						
					}// dar download novamente
				}
			}
		}
					
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
				boolean sucess;
			
				sucess=nodeUI.downloadFile(filename, hashCode, offset, packetLength, node);
				
				completed(node, offset, sucess);
				
				
			} catch (RemoteException e) {
				completed(node, offset, false);
			}
		}
		
		
		
	}

	public long getCurrentsize() {
		
		try {
			return (filesize - (missing.size() * packetLength))/1024;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public long getFilesize() {
		return filesize/1024;
	}


}
