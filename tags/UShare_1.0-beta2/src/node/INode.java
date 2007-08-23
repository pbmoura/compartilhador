package node;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INode extends Remote{
	public byte[] getFileParts(String hash,long offset,int length) throws RemoteException;
	public long getFileSize(String hash) throws RemoteException;
}
