package business;

import gui.MainFrame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.Vector;

import node.Node;
import supernode.SuperNode;
import util.Constants;

public class Controller {
	
	private static Controller instance = null;
	private Properties properties = null;
	private MainFrame frame;
	
	private Controller() {
		
	}
	
	public static Controller getInstance() {
		if(instance == null) {
			instance = new Controller();
		}
		return instance;
	}
	
	public void initNode(String args[]){
		try{
			properties = new Properties();
			Node.init(args);			
			initGUI();
		}catch(IOException ioe){
			MainFrame.showException(ioe.getMessage());
		}
	}
	
	public void initSuperNode(String args[]){
		try {
			new SuperNode(args[0],args[1], args[2]);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private void initGUI(){
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
		v.add(new FileInfo(new File("policy")));
		v.add(new FileInfo(new File("SplashImage.jpg")));
		v.add(new FileInfo(new File("window_ico.png")));
		
		return v;
		
	}
	public void startDownload(Object hash) {
		System.out.println("Controler.startDownload()");
		
	}
	public Vector getCurrentDownloads() {
		Vector v = new Vector();
		v.add(new FileInfo(new File("policy")));
		v.add(new FileInfo(new File("SplashImage.jpg")));
		v.add(new FileInfo(new File("window_ico.png")));
		
		return v;
	}
	


}
