package hxk.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


class Task implements Runnable{
    private int id;
    private Semaphore semaphore;//传递一个信号量来控制这个线程
    
    public Task(int id,Semaphore semaphore) {
	this.id = id;
	this.semaphore = semaphore;
    }


    @Override
    public void run() {
	try {
	    semaphore.acquire();//获取是否可以执行,若不行则等待
	    System.out.println(this);
	    semaphore.release();//执行完毕释放信号量
	} catch (InterruptedException e) {
	    System.out.println("Task Interrupted");
	}
    }

    @Override
    public String toString() {
	return "Task [id=" + id + "]";
    }
    
}

/**
 * @author Administrator
 * @description
 *2015-1-22  下午4:43:31
 */
public class SemaphoreDemo {
    public static void main(String[] args) throws InterruptedException {
	ExecutorService service = Executors.newCachedThreadPool();
	//构造参数的第二个值是公不公平对待等待线程的Boolean值..
	//若为TRUE,则等待(也就是调用了acquire的线程按照FIFO的方式等待)
	//若为FALSE,则是抢夺式的等待..体现为先调用acquire方法的线程不一定先执行
	Semaphore semaphore = new Semaphore(5, false);
	for (int i = 0; i < 20; i++) 
	    service.execute(new Task(i,semaphore));
	TimeUnit.SECONDS.sleep(2);
	service.shutdown();
	
    }
}
