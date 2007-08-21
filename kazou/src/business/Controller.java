package business;

import gui.MainFrame;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import util.Constants;

public class Controller {
	
	private static Controller instancia = null;
	private Properties properties = null;
	
	private Controller() {
		
	}
	
	public static Controller getInstancia() {
		if(instancia == null) {
			instancia = new Controller();
		}
		return instancia;
	}
	
//	public static void main(String args[]){
//		//TODO:check the place of this main()
//		init();		
//	}
	
	public void init(){
		properties = new Properties();
		initGUI();	
	}

	private void initGUI(){
		MainFrame frame = new MainFrame();
		frame.showScreen(Constants.SPLASH_SCREEN);	
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
		v.add(new FileInfo("policy"));
		v.add(new FileInfo("SplashImage.jpg"));
		v.add(new FileInfo("window_ico.png"));
		
		return v;
		
	}
	public void startDownload(Object hash) {
		System.out.println("Controler.startDownload()");
		
	}
	public Vector getCurrentDownloads() {
		Vector v = new Vector();
		v.add(new FileInfo("policy"));
		v.add(new FileInfo("SplashImage.jpg"));
		v.add(new FileInfo("window_ico.png"));
		
		return v;
	}
	


}
