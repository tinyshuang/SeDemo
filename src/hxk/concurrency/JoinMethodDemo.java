package hxk.concurrency;

import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @description
 *2015-1-16  下午4:07:18
 */
public class JoinMethodDemo {
    @SuppressWarnings("unused")
    public static void main(String[] args) {
	Sleep interSleep = new Sleep("inter"),
	      notInterSleep = new Sleep("notInter"); 	
	Joiner joiner = new Joiner("interJoiner", interSleep),
	       joiner2 = new Joiner("notInter", notInterSleep);
	interSleep.interrupt();
    }
}

/**
 *  执行结果:
 *  解释Join方法的作用: 比如在Joiner线程里面调用sleep.join()
 *  		          那么Joiner线程得等待sleep线程执行完才能执行..
 *  被中断的情况:
    inter---isInterrupted
    interJoiner---join completed
    
           一直执行下去的情况:
    sleep awake
    notInter---join completed
 */


class Sleep extends Thread{

    public Sleep(String name){
	super(name);
	start();
    }
    
    @Override
    public void run() {
	try {
	    TimeUnit.SECONDS.sleep(3);
	} catch (InterruptedException e) {
	    System.out.println(getName()+"---"+"isInterrupted");
	    return;
	}
	System.out.println("sleep awake");
    }
}

class Joiner extends Thread{
    private Sleep  sleep;
    
    public Joiner(String name,Sleep sleep){
	super(name);
	this.sleep = sleep;
	start();
    }

    @Override
    public void run() {
	try {
	    sleep.join();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	System.out.println(getName()+"---"+"join completed");
    }
}