package hxk.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Administrator
 * @description 初始化要做的事情..
 * 这个Runnable每执行一次run方法就latch.countDown()一次..所以计数器就减一..
 * 当latch.countDown()的count变为0的时候,意味着初始化的事情已经完成了..
 *2015-1-21  下午5:32:09
 */
class TaskPortion implements Runnable{
    private static int count = 0;
    private final int id = count++;
    private CountDownLatch latch;
    
    
    public TaskPortion(CountDownLatch latch) {
	this.latch = latch;
    }


    @Override
    public void run() {
	try {
	    TimeUnit.MILLISECONDS.sleep(1000);
	    System.out.println(this + " Task Complated");
	    latch.countDown();//计数器减一
	} catch (InterruptedException e) {
	    System.out.println(e + "Interrupted");
	}
    }

    @Override
    public String toString() {
	return "TaskPortion [id=" + id + "]";
    }
    
}

/**
 * 
 * @author Administrator
 * @description 初始化执行完毕后要做的事情
 *2015-1-21  下午5:34:42
 */
class WaitingTask implements Runnable{
    private static int count = 0;
    private final int id = count++;
    private CountDownLatch latch;
    
    public WaitingTask(CountDownLatch latch) {
	this.latch = latch;
    }

    @Override
    public void run() {
	try {
	    latch.await();//等待latch的count变为0才不阻塞
	    System.out.println("Latch barrier passed for " + this);
	} catch (InterruptedException e) {
	   System.out.println(this + " Interrupted");
	}
    }

    @Override
    public String toString() {
	return "WaitingTask [id=" + id + "]";
    }
    
}


/**
 * @author Administrator
 * @description
 *2015-1-21  下午5:06:18
 */
public class CountDownLatchDemo {
    public static final int SIZE = 100;
    
    public static void main(String[] args) {
	ExecutorService service = Executors.newCachedThreadPool();
	CountDownLatch latch = new CountDownLatch(SIZE);
	for (int i = 0; i < 10; i++) 
	    service.execute(new WaitingTask(latch));//初始化要执行的任务
	for (int i = 0; i < SIZE; i++) 
	    service.execute(new TaskPortion(latch));//初始化执行任务前所需的环境
	System.out.println("Lauch ALL Task");
	service.shutdown();
    }
}
