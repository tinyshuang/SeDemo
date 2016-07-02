public class PrimeProducer extends Thread {  
    private final BlockingQueue<BigInteger> queue;  
  
    PrimeProducer(BlockingQueue<BigInteger> queue) {  
        this.queue = queue;  
    }  
  
    public void run() {  
        try {  
            BigInteger p = BigInteger.ONE;  
            //中断是实现取消的最合理方式
            while (!Thread.currentThread().isInterrupted())  
                queue.put(p = p.nextProbablePrime());  
        } catch (InterruptedException consumed) {  
            /* Allow thread to exit */  
        }  
    }  
  
    public void cancel() {  
        interrupt();  
    }  
}  

//线程在阻塞状态下发生中断的时候会抛出InterruptedException，例如Thread.sleep(), Thread.wait(), Thread.join()等方法。

/**
Thread中的三个方法：

public void interrupt()   中断一个线程

public boolean isInterrupted()  获取中断标志，判断是否中断

public static boolean interrupted()  清楚中断状态，并返回它之前的状态值
*/
