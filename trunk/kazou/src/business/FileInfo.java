package business;

import java.io.*;

import util.MD5;

public class FileInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//These values does not change over time
	private File file;
	private long sizeInKB;
	private String hashValue;
	//These values  change over time
	private int tranferRate;
	private long completeKB=10;
	
	
	public FileInfo(File file) {
		this.file = file;
		try{
			this.sizeInKB = file.length()/1000; 
			this.hashValue = String.copyValueOf(MD5.encodeFile(file.getAbsolutePath()));		
			
		}catch(Exception e){
			e.printStackTrace();
		}	
		
	}
	
	
	public String getFilename() {
		return file.getName();
	}
	public void setFile(File file) {
		this.file = file;
	}
	public long getSizeInKB() {
		return sizeInKB;
	}
	public void setSizeInKB(long sizeInKB) {
		this.sizeInKB = sizeInKB;
	}
	public String getHashValue() {
		return hashValue;
	}
	public void setHashValue(String hashValue) {
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
	
	@Override
	public String toString() {
		
		return this.getFilename() + " " + this.getSizeInKB() + "KB";
	}
	
}
