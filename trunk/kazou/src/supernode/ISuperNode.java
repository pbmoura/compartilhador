package supernode;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface ISuperNode extends Remote{
	public void setNode(String NodeAddress, Vector filevector) throws RemoteException;
	public Vector getVector(String file) throws RemoteException;
	public Vector searchFile(String file, String name) throws RemoteException;
	public void disconnect(String name) throws RemoteException;
}
