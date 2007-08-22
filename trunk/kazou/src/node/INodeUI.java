package node;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

public interface INodeUI extends Remote {
	
	public void addMachines(List v) throws RemoteException;
	
	public void addFilesInfos(List v) throws RemoteException;
	
	public void finish() throws RemoteException;
	
	public void downloadFile(String nome, String hash, long startOffset,int totallength, INode ns) throws RemoteException;
	
	public List<String> getMachines() throws RemoteException;

}
