package supernode;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;

public interface ISuperNode extends Remote{
	public void setNode(String NodeAddress, Hashtable filehash) throws RemoteException;
//	public List getFileNodes(String file) throws RemoteException;
	public void searchFileByName(String file, String name) throws RemoteException;
	public List searchFileByHash(String hash, String name) throws RemoteException;
	public void disconnect(String name) throws RemoteException;
	public void addSuperNode(String name) throws RemoteException;
	public List<String> getSuperNodes() throws RemoteException;
}
