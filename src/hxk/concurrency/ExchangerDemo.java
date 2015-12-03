package hxk.concurrency;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Exchanger一个简单应用
 * 其典型应用场景是:一个任务在创建对象,而这些对象的生产代价很高昂,而另一个任务在消费这些对象
 * 通过这种方式,可以有更多的对象在被创建的时候被消费
 *2015-1-22  下午6:10:05
 */


class Product implements Runnable{
    private Exchanger<List<Integer>> exchanger;
    private List<Integer> holder;
    
    public Product(Exchanger<List<Integer>> exchanger,List<Integer> holder) {
	this.exchanger = exchanger;
	this.holder = holder;
    }



    @Override
    public void run() {
	try {
	    while (!Thread.interrupted()) {
		for (int i = 0; i < 10; i++) 
		    holder.add(i);//模拟生产
		System.out.println("Product --> " + holder);
		holder = exchanger.exchange(holder);//交换List
	    }
	} catch (InterruptedException e) {
	    System.out.println("Product Interrupted");
	}
    }
    
}

class Consume implements Runnable{
    private Exchanger<List<Integer>> exchanger;
    private List<Integer> holder;
    
    public Consume(Exchanger<List<Integer>> exchanger,List<Integer> holder) {
	this.exchanger = exchanger;
	this.holder = holder;
    }



    @Override
    public void run() {
	try {
	    while (!Thread.interrupted()) {
		holder = exchanger.exchange(holder);//消费..取得List
		for (int i = 0; i < 10; i++) 
		    for(Integer str:holder)
			holder.remove(str);//模拟消费List
		System.out.println("Consume--> " + holder);
	    }
	} catch (InterruptedException e) {
	    System.out.println("Consume Interrupted");
	}
    }
    
}

/**
 * @author Administrator
 * @description
 *2015-1-22  下午5:28:15
 */
public class ExchangerDemo {
    public static void main(String[] args) throws InterruptedException {
	ExecutorService service = Executors.newCachedThreadPool();
	Exchanger<List<Integer>> exchanger = new Exchanger<List<Integer>>();
	List<Integer> list = new CopyOnWriteArrayList<Integer>();//使用这个同步List,不会抛出ConcurrentModificationException
	service.execute(new Product(exchanger, list));
	service.execute(new Consume(exchanger, list));
	TimeUnit.MILLISECONDS.sleep(3);
	service.shutdownNow();
    }
}
