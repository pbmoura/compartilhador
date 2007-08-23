package util;

import java.io.*;
import java.security.*;
/*
 * Adapted from 
 * http://archives.java.sun.com/cgi-bin/wa?A2=ind0204&L=java-security&P=8977
 */
/**
 * This class implements a MD5Hash which hashes a file.
 *
 * To successfully hash an file and obtain a hash value, the
 * method should be used. The method takes in the input paramenter
 * as file path and returns the hash value as a String <code>hash</code>
 *
 *
 */

public class MD5 {

	/**
	 * Returns an file String which is the hashvalue for the specified
	input file path.
	 *
	 * @return  an String.
	 * @see     String
	 * @see     MessageDigest
	 * @see  FileInputStream
	 */
	public static char[] encodeFile(String filepath) {
		byte[] msg, msgSunDigest = null;
		/**
		 * Msg is the buffer for the message for which we want to
		 *
		 * compute the digest.
		 * MsgSunDigest is the buffer which will eventually contain the MD5
		digest.
		 */

		File f;
		
		FileInputStream fis;
		
		MessageDigest md5Sun = null;
		/**
		 *Md5Sun is the instance of the MD5 digest class
		 *Since it is a subclass of MessageDigest this declaration is ok.
		 */

		int nbytes, len;

		try {

			md5Sun = MessageDigest.getInstance("MD5");
			/**
			 *digest class which implements the MD5 algorithm.
			 *The first example asks for any implementation of MD5,
			 *and the second requests SUN's implementation, which is actually also
			 *the default.
			 */
			f = new File(filepath);
			msg = new byte[len = (int) f.length()];
			fis = new FileInputStream(f);
			if ((nbytes = fis.read(msg)) != len)
				throw new IOException(len + "unavailable.");
			fis.close();

			md5Sun.update(msg);
			msgSunDigest = md5Sun.digest();


		} catch (Exception e) {
			System.err.println(e + "MD5Test.encodeFile: some problems");			
		}
		return hexCodes(msgSunDigest);
	}
	
	public static char[] encodeChunk(byte[] chunk) {
		byte[] msgSunDigest = null;
				
		MessageDigest md5Sun = null;
		
		try {
			md5Sun = MessageDigest.getInstance("MD5");
			md5Sun.update(chunk);
			msgSunDigest = md5Sun.digest();
		} catch (Exception e) {
			System.err.println(e + "MD5Test.encodeChunk: some problems");			
		}
		return hexCodes(msgSunDigest);
	}
	
	/*
	 * From  http://www.devmedia.com.br/articles/viewcomp.asp?comp=2944&vt=-1
	 */
	private static char[] hexCodes(byte[] text) {

		char[] hexOutput = new char[text.length * 2];
		String hexString;
		for (int i = 0; i < text.length; i++) {
			hexString = "00" + Integer.toHexString(text[i]);
			hexString.toUpperCase().getChars(hexString.length() - 2,hexString.length(), hexOutput, i * 2);

		}

		return hexOutput;

	}


}
