package business;

import gui.MainFrame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import node.DownloadManager;
import node.Node;
import node.NodeUI;
import supernode.SuperNode;
import util.Constants;

public class Controller {
	
	private static Controller instance = null;
	private Properties properties =  new Properties();
	private NodeUI nodeUI = null;
	private MainFrame frame;
	
	private Controller() {
		
	}
	
	public static Controller getInstance() {
		if(instance == null) {
			instance = new Controller();
		}
		return instance;
	}
	

	public void initNode(String repository){
		try{			
			Node node = new Node(repository);
			nodeUI = node.getNodeUI();
		}catch(IOException ioe){
			showException(ioe.getMessage(),true);
			ioe.printStackTrace();
		}
	}
	
	public void initSuperNode(String repository){
		try {
			SuperNode supernode = new SuperNode(repository);
			nodeUI = supernode.getNodeUI();
			
		} catch (RemoteException e) {
			showException(e.getMessage(),true);
			e.printStackTrace();
		}	
	}

	public void initGUI(){
		this.frame = new MainFrame();
		this.frame.showScreen(Constants.SPLASH_SCREEN);	
	}
	
//	public static Vector getSuperNodeList() {
//		// TODO Auto-generated method stub
//		Vector v = new Vector();
//		v.add("localhost");
//		v.add("127.0.0.1");
//		return v;
//	}
	
	public void exit(int status){
		System.exit(0);
	}

	/**
	 * 
	 * @param arch Constants.NODE_ARCHITECTURE or Constants.SUPERNODE_ARCHITECTURE
	 */
	public void configureArch(int arch) {
		try {
		
		properties.setProperty("architecture",String.valueOf(arch));
		properties.store(new FileOutputStream("config.properties"), null);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }	
	}
	
	/**
	 * 
	 * @return Constants.NODE_ARCHITECTURE or Constants.SUPERNODE_ARCHITECTURE
	 */
	public int getArchitecture(){
		int arch=-1;
		try {
			properties.load(new FileInputStream("config.properties"));
			String strarch = properties.getProperty("architecture");
			if (strarch.length()!=0){
				arch= Integer.parseInt(strarch);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return arch;
	}
	
	public void configureUser(UserConfig userConfig) {    
		try {
	        properties.setProperty("repository", userConfig.getRepository());
//	        properties.setProperty("name", userConfig.getName());
//	        properties.setProperty("nameServer", userConfig.getNameServer());
			properties.store(new FileOutputStream("config.properties"), null);	        
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }		
	}

	public UserConfig getUserConfig() {
		UserConfig userConfig = null;
	    try {
	        properties.load(new FileInputStream("config.properties"));
	        userConfig = new UserConfig();
	        userConfig.setRepository(properties.getProperty("repository"));
//	        userConfig.setName(properties.getProperty("name"));
//	        userConfig.setNameServer(properties.getProperty("nameServer"));
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }		
	    return userConfig;
	}

	public List<FileInfo> searchFile(String fileName) {
		List<FileInfo> fileInfos = null;
		try {			
			nodeUI.search(fileName);
			fileInfos = nodeUI.getFilesInfos();

		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return fileInfos;
		
	}
	public void startDownload(String name, String hash) {
		System.out.println("Controler.startDownload()");
		try {
			nodeUI.download(name, hash);
		} catch (RemoteException e) {
			showException(e.getMessage(),true);
			e.printStackTrace();
		} catch (FileNotFoundException fe) {
			showException("Arquivo não encontrado",false);
		}
	}
	
	public void showException(String e,boolean exit){
		MainFrame.showException(e,exit);
	}
	public List<DownloadManager> getCurrentDownloads() {
		List<DownloadManager>currentDownloads= nodeUI.getCurrentDownloads();
		return currentDownloads;
	}

	public void initCommunications() {
		UserConfig userConfig = getUserConfig();
		if (getArchitecture() == Constants.NODE_ARCHITECTURE){
			initNode(userConfig.getRepository());
		} else {
			initSuperNode(userConfig.getRepository());
		}

		
	}
	


}
