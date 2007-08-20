package supernode;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

public interface ISuperNode extends Remote{
	public void setNode(String NodeAddress, List filevector) throws RemoteException;
	public List getFileNodes(String file) throws RemoteException;
	public List searchFile(String file, String name) throws RemoteException;
	public void disconnect(String name) throws RemoteException;
	public void addSuperNode(String name) throws RemoteException;
}
