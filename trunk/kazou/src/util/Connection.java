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
					writeIpsSuperNode(sNode.getSuperNodes());
					break;
					
				} catch (Exception e) {
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

	public void writeIpsSuperNode(List<String> list) throws IOException {
		FileOutputStream output = new FileOutputStream(sourceFile);
		
		list.add(0, ips.get(count));
		
		for(String s: ips) {
			for(String t: list) {
				if (s.equals(t)) {
					ips.remove(s);
					break;
				}
			}
		}
		
		list.addAll(ips);
		ips = list.subList(0, MAX_SIZE_LIST);
		String aux = "";
		
		for (String ip : ips) {
			aux = aux + ip + "\n";
		}
		
		output.write(aux.getBytes(), 0, aux.getBytes().length);
		output.flush();
	}
     
	public void removeIp(String ip ){
		ips.remove(ip);
	}
	
	public static void main(String args[]) {
		try {
			//new Connection();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
