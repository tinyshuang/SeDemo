package hxk.concurrency;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author Administrator
 * @description
 *2015-1-16  下午5:25:13
 */
public class ExceptionHandleDemo {
    public static void main(String[] args) {
	ExecutorService exec = Executors.newCachedThreadPool(new HandleThreadFactory());
	exec.execute(new ExceptionThreadDemo());
    }
}

class ExceptionThreadDemo implements Runnable{

    @Override
    public void run() {
	throw new RuntimeException();
    }
    
}

class MyUncaughExcetionHandel implements UncaughtExceptionHandler{

    @Override
    public void uncaughtException(Thread t, Throwable e) {
	System.out.println("caugh ---> " + e);
    }
}

class HandleThreadFactory implements ThreadFactory{

    @Override
    public Thread newThread(Runnable r) {
	System.out.println(this + " --> create new Thread");
	Thread thread = new Thread(r);
	thread.setUncaughtExceptionHandler(new MyUncaughExcetionHandel());
	System.out.println(this + " --> end ");
	return thread;
    }
    
}