package hxk.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Administrator
 * @description 使用MappedByteBuffer来处理大文件操作
 *2015-1-24  下午12:06:58
 */
public class LargeMapperFile {
    static int length = 0x8FFFFFF;//128M
    
    @SuppressWarnings("resource")
    public static void main(String[] args) throws FileNotFoundException, IOException {
	MappedByteBuffer out = new RandomAccessFile("d://data.txt", "rw").getChannel()
		.map(FileChannel.MapMode.READ_WRITE, 0, length);
	
	for (int i = 0; i < length; i++) 
	    out.put((byte)'x');
	System.out.println("Finish writing");
	
	for (int i = length; i < length/2+6; i++) 
	    System.out.println((char)out.get(i));
    }
}
