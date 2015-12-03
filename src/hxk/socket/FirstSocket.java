package hxk.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Administrator
 * @description 用Socket去访问网站并读取返回的数据
 *2015-1-29  下午6:20:30
 */
public class FirstSocket {
    public static void main(String[] args) {
	Socket socket = null;
	InputStream in = null;
	try {
	    socket = new Socket("time-A.timefreq.bldrdoc.gov",13);
	    in = socket.getInputStream();
	    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	    String text;
	    while((text=reader.readLine())!=null)
		System.out.println(text);
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	finally{
	    try {
		in.close();
		socket.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	
    }
}
