package hxk.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 这是一个错误的使用原子性的Demo
 * @description 由于getValue方法没同步,所以可以在任何时候返回数据..这当然包括在第一个++后..
 *2015-1-20  上午11:40:38
 */
public class AtomicWrongDemo implements Runnable{
    private volatile int i = 0;
    public int getValue() {return i;}
    private synchronized void add(){i++;i++;}
    public void run(){
	while (true) 
	    add();
    }
    
    public static void main(String[] args) {
	ExecutorService service = Executors.newCachedThreadPool();
	AtomicWrongDemo ad = new AtomicWrongDemo();
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
