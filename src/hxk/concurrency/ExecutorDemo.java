package hxk.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Administrator
 * @description
 *2015-1-16  下午12:13:54
 */
public class ExecutorDemo {
    public static void main(String[] args) {
	ExecutorService exec = Executors.newCachedThreadPool();
	for (int i = 0; i < 5; i++) 
	    exec.execute(new Count());
	exec.shutdown();
    }
}

class Count implements Runnable{
    private int count = 0;
    
    @Override
    public void run() {
	for (int i = 0; i < 10; i++) {
	    System.out.println(Thread.currentThread().getName()+"---"+count++);
	    //睡眠方式的正确写法
	    //TimeUnit.SECONDS.sleep(60);
	}
    }
    
}
