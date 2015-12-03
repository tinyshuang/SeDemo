package hxk.concurrency;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author Administrator
 * @description CyclicBarrier柵栏的一个简单的运用
 *2015-1-21  下午6:20:29
 */
class ReadyTask implements Runnable{
    private static int count = 0;
    private final int id = count++;
    
    private CyclicBarrier barrier;
    
    
    public ReadyTask(CyclicBarrier barrier) {
	this.barrier = barrier;
    }

    @Override
    public void run() {
	try {
	    System.out.println(this + " Ready Wait");//柵栏前的准备工作
	    //得当全部的线程都准备好才能跳过下面的阻塞..不然就一直直到线程数达到barrier的定义数量..才能跳过这道柵栏
	    //简单的理解就是必须是八匹马都到赛道前才能开始比赛..
	    barrier.await();
	    //达到条件后,不阻塞..跳过柵栏了..
	    System.out.println(this + " Working");
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Override
    public String toString() {
	return "ReadyTask [id=" + id + "]";
    }

}

/**
 * @author Administrator
 * @description
 *2015-1-21  下午6:11:56
 */
public class CyclicBarrierDemo {
    public static final int NUM = 5;
    
    public static void main(String[] args) {
	CyclicBarrier barrier = new CyclicBarrier(NUM, new Runnable() {
	    //定义的这个柵栏是当条件满足的时候执行的..
	    //当所有线程到达barrier时执行
	    @Override
	    public void run() {
		System.out.println("ReadyTask Ending..Starting the Working");
	    }
	});
	
	ExecutorService service = Executors.newCachedThreadPool();
	for (int i = 0; i < 5; i++) 
	    service.execute(new ReadyTask(barrier));
	service.shutdown();
    }
}
