package hxk.concurrency.cooperate;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Administrator
 * @description 一个使用管道IO来实现生产者-消费者模式的例子
 *2015-1-21  下午3:07:29
 */

/**
 * 
 * @author Administrator
 * @description 用PipedWriter来实现生产者
 *2015-1-21  下午3:08:03
 */
class Sender implements Runnable{
    private PipedWriter out = new PipedWriter();
    
    public PipedWriter getPipedWriter(){
	return out;
    }
    
    @Override
    public void run() {
	try {
            while (true) {
                for (char c = 'A'; c <= 'z';c++) {
                   out.write(c);
                   TimeUnit.MILLISECONDS.sleep(200);
                }
            }
	} catch (IOException e) {
	    System.out.println(e + "Sender Read Exception");
	} catch (InterruptedException e) {
	    System.out.println(e + "Sender Read Exception");
	}
    }
    
}

/**
 * 
 * @author Administrator
 * @description 用PipedReader(PipedWriter)来实现消费者
 *2015-1-21  下午3:08:28
 */
class Receiver implements Runnable{
    private PipedReader reader;
    
    
    public Receiver(Sender sender) throws IOException {
	this.reader = new PipedReader(sender.getPipedWriter());
    }


    @Override
    public void run() {
	try {
	    while (true) 
		System.out.println("Read " + (char)reader.read());
	    
	} catch (IOException e) {
	    System.out.println(e + "Recevied Read Exception");
	}
    }
    
}


/**
 * @author Administrator
 * @description
 *2015-1-21  下午2:54:14
 */
public class PipedIO {
    public static void main(String[] args) throws IOException, InterruptedException {
	Sender sender = new Sender();
	Receiver receiver = new Receiver(sender);
	ExecutorService service = Executors.newCachedThreadPool();
	service.execute(sender);
	service.execute(receiver);
	TimeUnit.SECONDS.sleep(3);
	service.shutdownNow();
    }
}
