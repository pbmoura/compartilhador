package business;

import gui.MainFrame;

import java.util.Vector;

import util.Constants;

public class Controler {
	
	public static void main(String args[]){
		//TODO:check the place of this main()
		init();		
	}
	public static void init(){
		//TODO: initialize communications
		initGUI();	
	}

	public static void initGUI(){
		MainFrame frame = new MainFrame();
		frame.showScreen(Constants.SPLASH_SCREEN);	
	}
	
	public static Vector getSuperNodeList() {
		// TODO Auto-generated method stub
		Vector v = new Vector();
		v.add("localhost");
		v.add("127.0.0.1");
		return v;
	}
	
	public static void exit(int status){
		//TODO: implement connections closing
		System.exit(0);
	}

	public static void configureUser(String userRepositoryPath, String supernodeAddress) {
		// TODO Auto-generated method stub
		
	}

	public static String getUserRepositoryPath() {
		// TODO Auto-generated method stub
		return "C:\\Windows\\";
	}

	public static Vector searchFile(String fileName) {
		// TODO Auto-generated method stub
		Vector v = new Vector();
		v.add(new FileInfo("policy"));
		v.add(new FileInfo("SplashImage.jpg"));
		v.add(new FileInfo("window_ico.png"));
		
		return v;
		
	}
	public static void startDownload(Object hash) {
		// TODO Auto-generated method stub
		System.out.println("Controler.startDownload()");
		
	}
	public static Vector getCurrentDownloads() {
		// TODO Auto-generated method stub
		Vector v = new Vector();
		v.add(new FileInfo("policy"));
		v.add(new FileInfo("SplashImage.jpg"));
		v.add(new FileInfo("window_ico.png"));
		
		return v;
	}
	


}
