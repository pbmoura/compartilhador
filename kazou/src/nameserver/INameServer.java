package nameserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface INameServer extends Remote{
	//Retorna o Endereço de um super no qualquer para cadastro
	public String getSuperNode() throws RemoteException;
	//Retorna o endereço do proximo super no para busca
	public Vector getSuperNodes() throws RemoteException;
	//Cadastro de super no
	public void setSuperNode(String address) throws RemoteException;
	//descadastra um super no
	public void disconnect(String name) throws RemoteException;
	
}
