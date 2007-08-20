package node;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

public interface INodeUI extends Remote {
	
	public void addMachines(List v) throws RemoteException;
	
	public void finish() throws RemoteException;

}
