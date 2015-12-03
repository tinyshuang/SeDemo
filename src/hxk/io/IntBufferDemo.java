package hxk.io;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * @author Administrator
 * @description 视图缓冲器
 *2015-1-24  上午11:10:31
 */
public class IntBufferDemo {
    public static final int BSIZE = 1024;
    
    public static void main(String[] args) {
	ByteBuffer buffer = ByteBuffer.allocate(BSIZE);
	IntBuffer ib = buffer.asIntBuffer();//转换为IntBuffer视图
	ib.put(new int[]{1,1,2,5,6,9,7,8,6});//可以直接储存数组了
	System.out.println(ib.get(6));//数组的基本操作
	ib.put(4, 78);
	ib.flip();//读之前的操作
	while (ib.hasRemaining()) 
	    System.out.println(ib.get());
    }
}
