package hxk.io;

import java.nio.ByteBuffer;

/**
 * @author Administrator
 * @description 获取通道的基本类型
 *2015-1-24  上午10:58:06
 */
public class GetData {
    public static final int BSIZE = 1024;
    
    public static void main(String[] args) {
	ByteBuffer buffer = ByteBuffer.allocate(BSIZE);
	
	//保存和读取char类型
	buffer.asCharBuffer().put("Hello World");
	char c;
	while ((c=buffer.getChar())!=0) 
	    System.out.print(c + "   ");
	buffer.rewind();//重置buffer
	
	//保存和读取int类型
	buffer.asIntBuffer().put(10101010);
	System.out.println(buffer.getInt());
	buffer.rewind();
	
    }
}
