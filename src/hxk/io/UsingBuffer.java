package hxk.io;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * @author Administrator
 * @description 使用通道交换字符串内的相邻奇偶字符
 *2015-1-24  上午 11:45:49
 */
public class UsingBuffer {
    private static void changeOddEvenValue(CharBuffer buffer){
	while (buffer.hasRemaining()) {
	    buffer.mark();
	    char c1 = buffer.get();
	    char c2 = buffer.get();
	    buffer.reset();
	    buffer.put(c2).put(c1);
	}
    }
    public static void main(String[] args) {
	char[] data = "UsingBuffers".toCharArray();
	ByteBuffer buffer = ByteBuffer.allocate(data.length * 2);
	CharBuffer cb = buffer.asCharBuffer();
	
	cb.put(data);
	
	System.out.println(cb.rewind());
	changeOddEvenValue(cb);
	System.out.println(cb.rewind());
	changeOddEvenValue(cb);
	System.out.println(cb.rewind());
    }
}
