package hxk.concurrency.cooperate;



import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//一个消费者和生产者的多线程的Demo

/**
 * 
 * @author Administrator
 * @description 一辆车需要涂蜡和抛光..
 * 抛光需要在涂蜡之后,第二次的涂蜡必须在抛光之后..就是有先后顺序的
 *2015-1-20  下午5:19:37
 */
class Car{
    private boolean waxOn = false;
    
    public synchronized void waxed(){//生产
	waxOn = true;
	notifyAll();
    }
    
    public synchronized void buffed(){//消费
	waxOn = false;
	notifyAll();
    }
    
    public synchronized void waitForWaxing() throws InterruptedException{
	while(waxOn == false)//必须使用循环来检查..如果notifyAll不一定唤醒的就是这个线程..
	    wait();	     //所以判断依据还是得是上面的那个条件..
    }
    
    public synchronized void waitForBuffing() throws InterruptedException{
	while(waxOn == true)
	    wait();
    }

}

/**
 * 
 * @author Administrator
 * @description 涂蜡的程序
 * 线程得内容是涂蜡然后等待抛光..如果抛光结束之后就是继续涂蜡..
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
	   System.out.println("Ending by a InterruptedException");
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
	   System.out.println("Ending by a InterruptedException");
	 }
	 System.out.println("Ending the buff task");	
    }
}

/**
 * @author Administrator
 * @description 描述汽车的涂蜡和抛光的线程之间的协作类
 *2015-1-20  下午4:53:01
 */
public class WaxCar {
    public static void main(String[] args) throws InterruptedException {
	Car car = new Car();
	ExecutorService service = Executors.newCachedThreadPool();
	service.execute(new WaxOn(car));
	service.execute(new WaxOff(car));
	TimeUnit.SECONDS.sleep(20);
	service.shutdownNow();
    }
}
