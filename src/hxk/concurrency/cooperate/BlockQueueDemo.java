package hxk.concurrency.cooperate;

import hxk.concurrency.cooperate.Toast.Stauts;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

//一个精彩的使用BlockQueue的例子
//让生产-消费者模式的多线程变得简单的类

/**
 * 
 * @author Administrator
 * @description 要消费的面包
 * 需要三个步骤--制作面包,涂黄油,涂果酱
 *2015-1-21  下午12:23:00
 */
class Toast{
    public enum Stauts{DRY,BUTTERED,JAMMED}//使用枚举的好例子
    private Stauts stauts = Stauts.DRY;
    private int id;
    
    public Toast(int id){this.id = id;}
    
    public void butter(){stauts = Stauts.BUTTERED;}
    public void jam(){stauts = Stauts.JAMMED;}
    public Stauts getStauts(){return stauts;}
    public int getId(){return id;}
    
    @Override
    public String toString() {
	return "Toast [stauts=" + stauts + ", id=" + id + "]";
    }
    
}

class ToastQueue extends LinkedBlockingQueue<Toast>{
    private static final long serialVersionUID = 9097760026206557826L;
}

/**
 * 
 * @author Administrator
 * @description 消费品的第一个步骤..制造面包
 *2015-1-21  下午12:35:56
 */
class Toaster implements Runnable{
    private ToastQueue queue;
    private int count = 0;
    
    public Toaster(ToastQueue queue){this.queue = queue;}
    
    @Override
    public void run() {
	try {
	    while (!Thread.interrupted()) {
		TimeUnit.MILLISECONDS.sleep(300);
		//制造消费品
		Toast toast = new Toast(count++);
		System.out.println(toast);
		//插入阻塞队列
		queue.put(toast);
	    }
	} catch (Exception e) {
	    System.out.println("Toaster Interrupted");
	}
	System.out.println("Toaster Off");
    }
    
}

/**
 * 
 * @author Administrator
 * @description 消费这件物品的第二个步骤..
 *2015-1-21  下午12:48:09
 */
class Butterer implements Runnable{
    private ToastQueue dry,butter;
    
    public Butterer(ToastQueue dry, ToastQueue butter) {
	this.dry = dry;
	this.butter = butter;
    }


    @Override
    public void run() {
	try {
	    while (!Thread.interrupted()) {
		Toast toast = dry.take();//从干面包队列中拿走
		System.out.println("Butter " + toast );
		toast.butter();//把干面包涂上黄油
		butter.put(toast);//把涂上黄油的面包放进黄油队列中
	    }
	} catch (Exception e) {
	    System.out.println("Butter Interrupted");
	}
	System.out.println("Butter Off");    
    }
    
}

/**
 * 
 * @author Administrator
 * @description 消费这件物品的第三个步骤..
 *2015-1-21  下午2:23:56
 */
class Jammer implements Runnable{
    private ToastQueue butter,finish;
    
    public Jammer(ToastQueue butter, ToastQueue finish) {
	this.butter = butter;
	this.finish = finish;
    }


    @Override
    public void run() {
	try {
	    while (!Thread.interrupted()) {
		Toast toast = butter.take();//从黄油队列中取出黄油面包
		toast.jam();//将黄油面包涂上果酱
		System.out.println("Jam " + toast );
		finish.put(toast);//将涂上果酱的面包放进果酱队列
	    }
	} catch (Exception e) {
	    System.out.println("Jar Interrupted");
	}
	System.out.println("Jar Off");  
    }
    
}


class Eater implements Runnable{
    private ToastQueue finish;
    private int count = 0;
    
    public Eater(ToastQueue finish) {
	this.finish = finish;
    }



    @Override
    public void run() {
	try {
	    while (!Thread.interrupted()) {
		Toast toast = finish.take();
		if (toast.getId() != count++ && toast.getStauts() == Stauts.JAMMED) {
		    System.out.println("Error");
		    System.exit(0);
		}
		else {
		    System.out.println("Eat it " + toast);
		}
		
	    }
	} catch (Exception e) {
	    System.out.println("Eat Interrupted");
	}
	System.out.println("Eat Off");	
    }
    
}

/**
 * @author Administrator
 * @description
 *2015-1-21  下午12:18:34
 */
public class BlockQueueDemo {
    public static void main(String[] args) throws InterruptedException {
	ToastQueue dry = new ToastQueue(),
		   butter = new ToastQueue(),
		   finish = new ToastQueue();
	ExecutorService service = Executors.newCachedThreadPool();
	service.execute(new Toaster(dry));
	service.execute(new Butterer(dry, butter));
	service.execute(new Jammer(butter, finish));
	service.execute(new Eater(finish));
	TimeUnit.SECONDS.sleep(5);
	service.shutdownNow();
    }
}
