package hxk.concurrency;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Administrator
 * @description
 *2015-1-20  下午12:12:58
 */
public class AtomicIntegerDemo implements Runnable{
    //使用了原子类
    private AtomicInteger ai = new AtomicInteger(0);
    private int getValue() {return ai.get();}
    private  void add(){ai.addAndGet(2);}
    public void run(){
	while (true) 
	    add();
    }
    
    public static void main(String[] args) {
	//由于本程序是正确的,所以写了一个定时器在5秒后来结束本程序
	new Timer().schedule(new TimerTask() {
	    @Override
	    public void run() {
		System.out.println("end the program");
		System.exit(0);
	    }
	}, 5000);
	
	ExecutorService service = Executors.newCachedThreadPool();
	AtomicIntegerDemo ad = new AtomicIntegerDemo();
	service.execute(ad);
	while (true) {
	    int value = ad.getValue();
	    if (value % 2 != 0) {
		System.out.println(value);
		System.exit(0);
	    }
	}
    }
}
