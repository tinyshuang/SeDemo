import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/**
 * @author Administrator
 * @description
 *2015-5-26  下午10:01:05
 */
public class LeftRhightDeadLock {
    private final Object left =  new Object();
    private final Object right = new Object();
    
    public void leftRight(){
	    synchronized (left) {
	      synchronized (right) {
	      	System.out.println("left");
	       }
	    }
    }
    
    public void rightLeft(){
	    synchronized (right) {
	         synchronized (left) {
	            System.out.println("right");
	        }   
      	} 
    }
    
    public static void main(String[] args) {
      	//得调用同一个对象
      	final LeftRhightDeadLock lock = new LeftRhightDeadLock();
      	ExecutorService service = Executors.newFixedThreadPool(50);
      	for (int i = 0; i < 30; i++) {
      	    service.execute(new Runnable() {
      		    @Override
      		    public void run() {
      		    	lock.leftRight();
      		    }
      		});
      		service.execute(new Runnable() {
      		    @Override
      		    public void run() {
      			    lock.rightLeft();
      		    }
      		}); 
      	}
      	service.shutdown();
 }
}
