package util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Net {

	public static String getLocalIPAddress() throws IOException {
		String ip = "";
		
		Enumeration<NetworkInterface> interfaces = 
			NetworkInterface.getNetworkInterfaces();
		
		while (interfaces.hasMoreElements()) {
			Enumeration<InetAddress> inetAddresses = 
				interfaces.nextElement().getInetAddresses();
			
			while(inetAddresses.hasMoreElements()) {
				InetAddress inet = inetAddresses.nextElement();
				
				if(!inet.isLoopbackAddress()) {
					String hostAddress = inet.getHostAddress();
					
					if(hostAddress.matches(
							"\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"))
						ip = hostAddress;
				}
			}
		}
		
		return ip;
	}
	
	public static void main(String[] args) throws IOException {
		Net.getLocalIPAddress();
	}
	
}
