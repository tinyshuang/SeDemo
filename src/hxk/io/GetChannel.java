package hxk.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Administrator
 * @description
 *2015-1-24  上午9:59:06
 */
public class GetChannel {
    public static final int BSIZE = 1024;
    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException {
	String path = "d://data.txt";
	//使用FileOutputStream提供的通道写一个字符串写进文件
	FileChannel fc = new FileOutputStream(path).getChannel();
	fc.write(ByteBuffer.wrap("Hello World".getBytes()));
	fc.close();
	
	//使用RandomAccessFile提供的通道操作文件
	fc = new RandomAccessFile(path, "rw").getChannel();
	fc.position(fc.size());
	fc.write(ByteBuffer.wrap("Hello Everybody".getBytes()));
	fc.close();
	
	//使用FileInputStream提供的通道读取文件
	fc = new FileInputStream(path).getChannel();
	ByteBuffer buffer = ByteBuffer.allocate(BSIZE);//对于只读访问,必须显式的使用allocate来分配bytebuffer
	fc.read(buffer);
	buffer.flip();//调用read()就必须调用flip()来做好让别人读取的准备
	while(buffer.hasRemaining())
	    System.out.println((char)buffer.get());
    }
}
