package business;

import java.util.Vector;

public class Controler {

	public static Vector getSuperNodeList() {
		// TODO Auto-generated method stub
		Vector v = new Vector();
		v.add("localhost");
		v.add("127.0.0.1");
		return v;
	}
	
	public static void exit(int status){
		//TODO: implemtent connections closing
		System.exit(0);
	}

	public static void configureUser(String userRepositoryPath, String supernodeAddress) {
		// TODO Auto-generated method stub
		
	}

	public static String getUserRepositoryPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void searchFile(String fileName) {
		// TODO Auto-generated method stub
		
	}

}
