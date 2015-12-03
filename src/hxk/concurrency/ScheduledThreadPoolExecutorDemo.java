package hxk.concurrency;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//一个使用ScheduledThreadPoolExecutor的schedule与scheduleAtFixedRate方法的Demo

/**
 * @author Administrator
 * @description 一个温室控制器
 *2015-1-22  下午3:12:53
 */
public class ScheduledThreadPoolExecutorDemo {
    @SuppressWarnings("unused")
    private volatile boolean light = false;
    @SuppressWarnings("unused")
    private volatile boolean water = false;
    
    ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(10);
    
    /**
     * @description 计划多久(delay)后执行一件事	
     * @param event 一段时间后要执行的任务
     * @param time  计划多久之后执行的时间 
     *2015-1-22  下午3:35:45
     *返回类型:void
     */
    public void schedule(Runnable event,long time){
	scheduled.schedule(event, time, TimeUnit.MILLISECONDS);
    }
    
    /**
     * @description 一段延迟之后以固定的时间差重复执行某个任务	
     * @param event 任务
     * @param initDelay 第一次执行任务的延迟的时间
     * @param period 延迟之后的固定周期
     *2015-1-22  下午3:41:48
     *返回类型:void
     */
    public void repeat(Runnable event,long initDelay,long period){
	scheduled.scheduleAtFixedRate(event, initDelay, period, TimeUnit.MILLISECONDS);
    }
    
    class LightOn implements Runnable{
	@Override
	public void run() {
	    System.out.println("Turn On Light");
	    light = true;
	}
    }
    
    class LightOff implements Runnable{
	@Override
	public void run() {
	    System.out.println("Turn Off Light");
	    light = false;
	}
    }
    
    class WaterOn implements Runnable{
	@Override
	public void run() {
	   System.out.println("Turn On Water");
	   water = true;
	}
    }

    class WaterOff implements Runnable{
	@Override
	public void run() {
	    System.out.println("Turn Off Water");
	    water = false;
	}
    }
    
    class Bell implements Runnable{
	@Override
	public void run() {
	    System.out.println("Bell");
	}
    }
    
    public static void main(String[] args) {
	ScheduledThreadPoolExecutorDemo sh = new ScheduledThreadPoolExecutorDemo();
	sh.schedule(sh.new Bell(), 10000);
	sh.repeat(sh.new WaterOn(), 0, 2000);
	sh.repeat(sh.new WaterOff(), 1000, 2000);
	sh.repeat(sh.new LightOn(), 0, 2000);
	sh.repeat(sh.new LightOff(), 1000, 2000);
    }
}
