package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;

import supernode.ISuperNode;
import supernode.SuperNode;

public class Connection {
	
	private LineNumberReader reader;
	private File sourceFile;
	private List<String> ips;
	private int count;
	private String name;
	public static final int MAX_SIZE_LIST = 20;

	public Connection(String name)  {
		this.name = name;
	}
	
	private void init() throws IOException {
		openFile();
		ips = getListIps();
		count = ips.size()-1;
	}
	
	/**
	 * Tenta conectar a algum supernode da lista de supernodes.
	 * @return
	 */
	public String connect() {
		try {
			String ip;
			String firstIP;
			
			if (ips == null)
				init();
			
			ip = nextIp();
			firstIP = ip;
			
			while(true) {
								
				String supNode = "//" + ip + "/supernode";
				ISuperNode sNode;
				
				try {
					
					sNode = (ISuperNode) Naming.lookup(supNode);
					System.out.println("Conectado a " + ip);
					this.name = ip;
					updateIpListFile(sNode.getSuperNodes());
					break;
					
				} catch (Exception e) {
					e.printStackTrace();
					
					ip = nextIp();
					
					if (ip.equals(firstIP))
						return null;
					
					continue;
				}
			}
			
			return ip;
		} catch (IOException ioe) {
			return null;
		}
		
	}

	private void openFile() throws IOException {

		sourceFile = new File("supernodes.list");
		if (sourceFile.exists() && sourceFile.canRead())
			reader = new LineNumberReader(new FileReader(sourceFile));
		else
			throw new IOException("File not found");
	}

	public String nextIp() throws IOException {
		
		try {
			
			if (count + 1 == ips.size())
				count = 0;
			else
				count++;
			
			String ip = ips.get(count);
			return ip;
		} catch (Exception e) {
			return null;
		}
	}

	public List<String> getListIps() throws IOException {
		List<String> lista = new ArrayList<String>();
		while (true) {
			String ip = reader.readLine();
			if (ip == null)
				return lista;
			lista.add(ip);
		}
	}

	public void updateIpListFile(List<String> superNodeIpList) throws IOException {
		FileOutputStream output = new FileOutputStream(sourceFile);
		List<String> newIpList = new ArrayList<String>();
		
		newIpList.add(this.name);
		
		for (String s : superNodeIpList) {
			if (superNodeIpList.indexOf(s) < MAX_SIZE_LIST &&
					!newIpList.contains(s))
				newIpList.add(s);
		}
		
		if (newIpList.size() < MAX_SIZE_LIST) {
			for (String s : this.ips) {
				if (this.ips.indexOf(s) < MAX_SIZE_LIST && 
						!newIpList.contains(s))
					newIpList.add(s);
			}
		}
		
		String outputString = "";
		
		for (String ip : newIpList) {
			outputString += ip + "\n";
		}
		
		output.write(outputString.getBytes(), 0, outputString.getBytes().length);
		output.flush();
		output.close();
	}
	
}
