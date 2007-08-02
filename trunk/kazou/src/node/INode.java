package node;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INode extends Remote{
	public String getFile(String file) throws RemoteException;
}
