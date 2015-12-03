package hxk.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author Administrator
 * @description 处理一个通道与另一个通道直接相连的最佳方式
 *2015-1-24  上午10:26:46
 */
public class TransferTo {
    public static final int BSIZE = 1024;
    
    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException {
	FileChannel in = new FileInputStream("d://data.txt").getChannel(),
		    out = new FileOutputStream("d://data2.txt").getChannel();
	
	in.transferTo(0, in.size(), out);
	 //or:
	//out.transferFrom(in,0,in.size());
    }
}
