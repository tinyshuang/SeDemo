package hxk.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//使用Lock的Demo
//一个消费者和生产者的多线程的使用Lock的Demo

/**
 * 
 * @author Administrator
 * @description 一辆车需要涂蜡和抛光..
 * 抛光需要在涂蜡之后,第二次的涂蜡必须在抛光之后..就是有先后顺序的
 *2015-1-20  下午5:19:37
 */
class Car{
    private Lock lock = new ReentrantLock();//定义一个锁
    private Condition condition = lock.newCondition();//使用上面的锁对象生成一个Condition
    private boolean waxOn = false;
    
    public  void waxed(){
	lock.lock();//每个方法的第一行都是先锁住
	try {
	    waxOn = true;
	    condition.signalAll(); //利用Condition去通知
	} finally{
	    lock.unlock();//最后都是要释放锁
	}
    }
    
    public  void buffed(){
	lock.lock();
	try {
	    waxOn = false;
	    condition.signalAll(); 
	} finally{
	    lock.unlock();
	}
    }
    
    public  void waitForWaxing() throws InterruptedException{
	lock.lock();
	try {
	    while(waxOn == false)//必须使用循环来检查..如果notifyAll不一定唤醒的就是这个线程..
		condition.await();//所以判断依据还是得是上面的那个条件..
	}finally{
	    lock.unlock();
	}
	
    }
    
    public  void waitForBuffing() throws InterruptedException{
	lock.lock();
	try {
	    while(waxOn == true)//必须使用循环来检查..如果notifyAll不一定唤醒的就是这个线程..
		condition.await();//所以判断依据还是得是上面的那个条件..
	}finally{
	    lock.unlock();
	}
    }

}

/**
 * 
 * @author Administrator
 * @description 涂蜡的程序
 * 线程的内容是涂蜡然后等待抛光..如果抛光结束之后就是继续涂蜡..
 *2015-1-20  下午5:21:48
 */
class WaxOn implements Runnable{
    private Car car;
    public WaxOn(Car car){this.car = car;}
    @Override
    public void run() {
       try {
           while (!Thread.interrupted()) {
               System.out.println("Wax on");
               TimeUnit.SECONDS.sleep(2);
               car.waxed();
               car.waitForBuffing();
           }
       } catch (InterruptedException e) {
	   System.out.println("Ending by a Interrupted");
       }
       System.out.println("Ending the wax task");
    }
    
}

/**
 * 
 * @author Administrator
 * @description 抛光..
 * 程序执行的是抛光,然后等待涂蜡..在涂蜡完成后接着抛光
 *2015-1-20  下午5:23:29
 */
class WaxOff implements Runnable{
    private Car car;
    public WaxOff(Car car){this.car = car;}   
    
    @Override
    public void run() {
	try {
	    while (!Thread.interrupted()) {
		car.waitForWaxing();//注意这里的执行顺序..消费者的抛光要等待先
	        System.out.println("Wax off");
	        TimeUnit.SECONDS.sleep(2);
	        car.buffed();//然后才可以消费
	    }
	 } catch (InterruptedException e) {
	   System.out.println("Ending by a Interrupted");
	 }
	 System.out.println("Ending the buff task");	
    }
}

/**
 * @author Administrator
 * @description 描述汽车的涂蜡和抛光的线程之间的协作类
 *2015-1-20  下午4:53:01
 */
public class WaxCarLock {
    public static void main(String[] args) throws InterruptedException {
	Car car = new Car();
	ExecutorService service = Executors.newCachedThreadPool();
	service.execute(new WaxOn(car));
	service.execute(new WaxOff(car));
	TimeUnit.SECONDS.sleep(20);
	service.shutdownNow();
    }
}
