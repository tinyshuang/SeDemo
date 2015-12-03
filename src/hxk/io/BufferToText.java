package hxk.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 缓冲器容纳的是普通的字节,为了把它们转换为字符,我们要么在输入它们的时候对其进行编码,要么在其取出缓冲器的时候对其进行解码
 */

/**
 * @author Administrator
 * @description
 *2015-1-24  上午10:41:40
 */
public class BufferToText {
    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException {
	FileChannel in = new FileInputStream("d://data.txt").getChannel(),
		    out = new FileOutputStream("d://data.txt").getChannel();
	
	ByteBuffer buffer = ByteBuffer.allocate(1024);
	//buffer.asCharBuffer().put("content");
	//out.write(buffer);
	
	out.write(ByteBuffer.wrap("Hello ASDFGHJJKLL".getBytes("UTF-16BE")));
	out.close();
	
	buffer.clear();
	in.read(buffer);
	buffer.flip();
	System.out.println(buffer.asCharBuffer());
    }
}
