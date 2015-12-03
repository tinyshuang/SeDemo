package hxk.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Administrator
 * @description Callable接口的demo
 * Callable-->Future<?>-->future.get()
 *2015-1-16  下午12:48:33
 */
public class CallableDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
	ExecutorService exec = Executors.newCachedThreadPool();
	List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
	for (int i = 0; i < 5; i++) 
	    futures.add(exec.submit(new Num()));
	for (Future<Integer> future : futures) 
	    System.out.println(future.get());//get方法是阻塞的..
	exec.shutdown();
    }
    
    @Override
    public String toString(){
	return Thread.currentThread().getName();
    }
}

class Num implements Callable<Integer>{
    private static int count = 0;
    
    @Override
    public Integer call() throws Exception {
	return count++;
    }
    
}
