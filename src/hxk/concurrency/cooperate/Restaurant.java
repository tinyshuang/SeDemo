package hxk.concurrency.cooperate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Administrator
 * @description 生产者和消费者之间的物品
 *2015-1-21  上午10:12:56
 */
class Meal{
    private final int orderNum;

    public Meal(int orderNum) {
	this.orderNum = orderNum;
    }

    @Override
    public String toString() {
	return "Meal [orderNum=" + orderNum + "]";
    }
    
}

/**
 * 
 * @author Administrator
 * @description 消费者
 *2015-1-21  上午10:21:37
 */
class WaitPerson implements Runnable{
    private  Restaurant restaurant;

    public WaitPerson(Restaurant restaurant) {
	this.restaurant = restaurant;
    }

    @Override
    public void run() {
	try {
	    while (!Thread.interrupted()) {
		synchronized (this) {
		    while (restaurant.meal == null) 
			wait();//当要消费的物品为空时就一直等待
		}
		//当不为空时就跳出等待的循环
		System.out.println("meals consume"+restaurant.meal);
		//锁住生产者
		synchronized (restaurant.chef) {
		    restaurant.meal = null;//消费物品
		    restaurant.chef.notifyAll();//通知生产者接着生成..restaurant.chef表示通知这个对象上的锁可以释放了
		}
	    }
	} catch (InterruptedException e) {
	    System.out.println("Waiter Interrupted");
	}
    }
    
}

/**
 * 
 * @author Administrator
 * @description 生产者
 *2015-1-21  上午10:33:03
 */
class Chef implements Runnable{
    private Restaurant restaurant;
    private int count = 0;//计数生产的第几块肉
    public Chef(Restaurant restaurant) {
	this.restaurant = restaurant;
    }


    @Override
    public void run() {
	try {
	    while (!Thread.interrupted()) {
		synchronized (this) {
		    while (restaurant.meal != null) 
			wait();//消费者当物品不为空时,一直等待
		}
		//当物品超出十个时跳出
		if (++count == 10) {
		    System.out.println("Out of food,Closing");
		    restaurant.service.shutdownNow();
		}
		System.out.println("meals ready");
		//生产物品,以及通知消费者开始消费
		synchronized (restaurant.wait) {
		    restaurant.meal = new Meal(count);
		    restaurant.wait.notifyAll();
		}
		TimeUnit.MILLISECONDS.sleep(100);
	    }
	} catch (InterruptedException e) {
	   System.out.println("Chef Interrupted");
	}
    }
    
}

/**
 * @author Administrator
 * @description Restaurant是生产者和消费者之间的焦点,生产者和消费者都必须知道这个焦点
 *2015-1-21  上午10:11:36
 */
public class Restaurant {
    Meal meal;//消费的物品
    ExecutorService service = Executors.newCachedThreadPool();
    WaitPerson wait = new WaitPerson(this);
    Chef chef = new Chef(this);
    
    public Restaurant() {
	//构造器里面启动线程
	service.execute(wait);
	service.execute(chef);
    }
    
    public static void main(String[] args) {
	new Restaurant();
    }
}
