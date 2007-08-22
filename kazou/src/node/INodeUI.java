package node;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import business.FileInfo;

public interface INodeUI extends Remote {
	
	public void addMachines(List<String> v) throws RemoteException;
	
	public void addFilesInfos(List<FileInfo> v) throws RemoteException;
	
	public void finish() throws RemoteException;
	
	public boolean downloadFile(String nome, String hash, long startOffset,int totallength, INode ns) throws RemoteException;
	
	public List<String> getMachines() throws RemoteException;

}
