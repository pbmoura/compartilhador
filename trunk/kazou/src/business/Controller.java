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

import node.Node;
import node.NodeUI;
import supernode.SuperNode;
import util.Constants;
import util.Net;

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
	

	public void initNode(String superNodeIP, String repository){
		try{			
			Node node = new Node(superNodeIP,repository);
			nodeUI = node.getNodeUI();
		}catch(IOException ioe){
			showException(ioe.getMessage(),true);
			ioe.printStackTrace();
		}
	}
	
	public void initSuperNode(String superNode, String repository){
		try {
			SuperNode supernode = new SuperNode(superNode, repository);
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
			arch= Integer.parseInt(properties.getProperty("architecture"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return arch;
	}
	
	public void configureUser(UserConfig userConfig) {    
		try {
	        properties.setProperty("repository", userConfig.getRepository());
	        properties.setProperty("name", userConfig.getName());
	        properties.setProperty("nameServer", userConfig.getNameServer());
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
	        userConfig.setName(properties.getProperty("name"));
	        userConfig.setNameServer(properties.getProperty("nameServer"));
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }		
	    return userConfig;
	}

	public Vector searchFile(String fileName) {
		Vector v = new Vector();
		try {			
			nodeUI.search(fileName);
			List<FileInfo> result = nodeUI.getFilesInfos();
			for (FileInfo fileInfo : result) {
				v.add(fileInfo);
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	
//		v.add(new FileInfo(new File("policy")));
//		v.add(new FileInfo(new File("SplashImage.jpg")));
//		v.add(new FileInfo(new File("window_ico.png")));
		
		return v;
		
	}
	public void startDownload(String name, String hash) {
		System.out.println("Controler.startDownload()");
		try {
			nodeUI.download(name, hash);
		} catch (RemoteException e) {
			showException(e.getMessage(),true);
			e.printStackTrace();
		} catch (FileNotFoundException fe) {
			showException("Arquivo n�o encontrado",false);
		}
	}
	
	public void showException(String e,boolean exit){
		MainFrame.showException(e,exit);
	}
	public Vector getCurrentDownloads() {
		Vector v = new Vector();
		v.add(new FileInfo(new File("policy")));
		v.add(new FileInfo(new File("SplashImage.jpg")));
		v.add(new FileInfo(new File("window_ico.png")));
		
		return v;
	}

	public void initCommunications() {
		UserConfig userConfig = getUserConfig();
		if (getArchitecture() == Constants.NODE_ARCHITECTURE){
			initNode(userConfig.getNameServer(), userConfig.getRepository());
		} else {
			initSuperNode(userConfig.getNameServer(), userConfig.getRepository());
		}

		
	}
	


}
