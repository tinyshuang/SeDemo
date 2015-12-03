package hxk.socket;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Administrator
 * @description
 *2015-1-30  上午10:04:10
 */
public class InetAddressDemo {
    public static void main(String[] args) {
	getLocalAddress();
    }
    
    public static void getAddress(String host){
	try {
	    InetAddress[] address = InetAddress.getAllByName(host);
	    for (InetAddress inetAddress : address) 
		System.out.println(inetAddress);
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	}
    }
    
    public static void getLocalAddress(){
	try {
	    InetAddress address = InetAddress.getLocalHost();
	    System.out.println(address.getHostAddress());
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	}
    }
}
