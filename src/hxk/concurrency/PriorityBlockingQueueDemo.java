package hxk.concurrency;

import java.util.concurrent.PriorityBlockingQueue;

//一个阻塞的优先级队列PriorityBlockingQueue

/**
 * 
 * @author Administrator
 * @description 要进行优先级排列的实体
 *2015-1-22  下午2:20:57
 */
class Bus implements Comparable<Bus>{  
    
    private String busNo; 
    private Integer busType;  
    private Integer level;//用来代表优先级的,compare接口的compareTo方法里面比较的属性  

    public Bus(String busNo, Integer busType, Integer level) {  
        this.busNo = busNo;  
        this.busType = busType;  
        this.level = level;  
    }  

    public String getBusNo() {  
        return busNo;  
    }  

    public void setBusNo(String busNo) {  
        this.busNo = busNo;  
    }  

    public Integer getBusType() {  
        return busType;  
    }  

    public void setBusType(Integer busType) {  
        this.busType = busType;  
    }  
      

    public Integer getLevel() {  
        return level;  
    }  

    public void setLevel(Integer level) {  
        this.level = level;  
    }  

    /**
     * PriorityBlockingQueue的优先级依靠这个方法
     */
    @Override  
    public int compareTo(Bus o) {  
        if(o instanceof Bus){  
            return (level>o.level)?1:-1;  
        }  
        return 0;  
    }  

    @Override  
    public String toString() {  
        return "当前车信息：种类["+busType+"]车牌["+busNo+"]优先级["+level+"]";  
    }  
}  


/**
 * @author Administrator
 * @description
 *2015-1-22  下午2:20:11
 */
public class PriorityBlockingQueueDemo {
    private static final int MAX_PARKING = 50;  
    private static final PriorityBlockingQueue<Bus> station = new PriorityBlockingQueue<Bus>(MAX_PARKING);  
    
    public static void main(String[] args) throws InterruptedException {  
        PriorityBlockingQueueDemo instance = new PriorityBlockingQueueDemo();  
        
        //进行各种各种的车辆进站..
        instance.enter(new Bus("粤A12345", 2, 5));  
        instance.enter(new Bus("粤A88888", 1, 2));  
        instance.enter(new Bus("粤A66666", 2, 6));  
        instance.enter(new Bus("粤A33333", 1, 7));  
        instance.enter(new Bus("粤A21123", 2, 1));  
        instance.enter(new Bus("粤AGG892", 1, 4));  
        instance.enter(new Bus("粤AJJ000", 2, 9));  
          
        try {
            while(!Thread.interrupted()){  
                instance.quit();  
            }
	} catch (InterruptedException e) {
	    System.out.println("Bus Quit Interrupted");
	}
          
    }  
      
      
    private void enter(Bus bus){  
        if(station.size()<MAX_PARKING)  
            station.add(bus);  
        else  
            System.out.println("站内车位已满");  
    }  
      
    /** 
     * 车辆出站 
     * @throws InterruptedException 当车站没有车还打算出车时抛出此异常
     */  
    private void quit() throws InterruptedException{  
       if(station.size()==0) 
	   throw new InterruptedException();
       Bus bus = station.take(); 
       System.out.println("出站-->"+bus.toString());  
    } 
}
