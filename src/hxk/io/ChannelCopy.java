package hxk.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Administrator
 * @description 运用通道的简单读取与写入
 *2015-1-24  上午10:19:10
 */
public class ChannelCopy {
    public static final int BSIZE = 1024;
    
    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException {
	FileChannel in = new FileInputStream("d://data.txt").getChannel(),
		    out = new FileOutputStream("d://data2.txt").getChannel();
	
	ByteBuffer buffer = ByteBuffer.allocate(BSIZE);
	
	while (in.read(buffer)!= -1) {//把内容读进通道
	    buffer.flip();//准备写
	    out.write(buffer);//把内容从通道搬出来,写进IO
	    buffer.clear();//清除通道,为了下一次写
	}
    }
}
