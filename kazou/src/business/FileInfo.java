package business;

import java.io.*;

import util.MD5;

public class FileInfo {
	//These values does not change over time
	private String filename;
	private long sizeInKB;
	private char[] hashValue;
	//These values  change over time
	private int tranferRate;
	private long completeKB=10;
	
	
	public FileInfo(String filename) {
		this.filename = filename;
		try{
			this.sizeInKB = new File(filename).length()/1000; 
			this.hashValue = MD5.encodeFile(filename);		
			
		}catch(Exception e){
			e.printStackTrace();
		}	
		
	}
	
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getSizeInKB() {
		return sizeInKB;
	}
	public void setSizeInKB(long sizeInKB) {
		this.sizeInKB = sizeInKB;
	}
	public char[] getHashValue() {
		return hashValue;
	}
	public void setHashValue(char[] hashValue) {
		this.hashValue = hashValue;
	}
	public int getTranferRate() {
		return tranferRate;
	}
	public void setTranferRate(int tranferRate) {
		this.tranferRate = tranferRate;
	}
	public long getCompleteKB() {
		return completeKB;
	}
	public void setCompleteKB(long completeKB) {
		this.completeKB = completeKB;
	}
	
	
	
}
