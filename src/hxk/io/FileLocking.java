package hxk.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @description 文件加锁机制
 * 通过调用FileChanle的tryLock或者lock就能给文件加锁
 * tryLock是阻塞的,如果不能获得锁,则将直接返回
 * lock是阻塞的,它要阻塞到获得锁为止
 *2015-1-24  下午12:19:55
 */
public class FileLocking {
    public static void main(String[] args) throws InterruptedException, IOException {
	FileOutputStream out = new FileOutputStream("d://data.txt");
	FileLock lock = out.getChannel().tryLock();
	
	if (lock!=null) {
	    System.out.println("Lock File");
	    TimeUnit.SECONDS.sleep(1);
	    lock.release();
	    System.out.println("Release Lock");
	    
	}
	out.close();
    }
}
