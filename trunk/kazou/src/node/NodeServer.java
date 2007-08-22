/*
 * Created on 18/06/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package node;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import util.MD5;


import business.FileInfo;



/**
 * @author rodrigo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NodeServer extends UnicastRemoteObject implements INode {
	
	private String ip;
	private String repository;
	private Hashtable<String, String> filesHash;
	
	public NodeServer(String ip, String repository) throws RemoteException {
		super();
		this.ip = ip;
		this.repository = repository;
		this.filesHash = new Hashtable<String, String>();
		init();
	}
	
	public NodeServer() throws RemoteException {
		super();
	}
	
	private void init () {
		System.out.println("Imprimindo name..."+ip);
		fillHash();
		System.out.println("Carregando servidor de arquivos...");
		
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
	    }
		
		String objname = "//"+this.ip+"/nodeserver";
		try {
			Naming.rebind(objname, this);
			System.out.println("servidor de arquivos pronto");
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	private void fillHash() {
		File folder = new File(repository);
		File [] folderList = folder.listFiles();
		
		
		for (int i=0;i<folderList.length;i++) {
			String hash = String.valueOf(MD5.encodeFile(folderList[i].getAbsolutePath()));
			filesHash.put(hash, folderList[i].getName());
		}
		
	}
		
	public byte[] getFileParts(String hash,long offset,int length){
		String nome = filesHash.get(hash);
		
		File f=new File(repository+File.separator+nome);
		byte[] buffer=null;
		
		
		if(offset>f.length())
			return null;
		
		if(offset +length>f.length())
			buffer=new byte[(int)(f.length()-offset)];
		else
		    buffer=new byte[length];
		
		
		
		DataInputStream dis=null;
		try {
			
			dis=new DataInputStream(new FileInputStream(repository+File.separator+nome));
			
			System.out.println(dis.skip(offset));
			
			try {
				dis.readFully(buffer);
				return buffer;
			}catch (EOFException e) {
				System.out.println("EOF "+offset);
				return buffer;
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * tamanho do arquivo
	 * @param nome
	 * @return
	 */
	public long getFileSize(String hash){
		String name = filesHash.get(hash);
		File f= new File(repository+File.separator+name);
		if(f.exists())
			return f.length();
		else 
			return -1;
	}
}
