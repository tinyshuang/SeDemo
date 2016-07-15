/**
*基于Lock的Semaphore实现
*/
public class SemaphoreOnLock {
       private final Lock lock = new ReentrantLock();
       //条件：permits > 0
       private final Condition permitsAvailable = lock.newCondition();
       private int permits;//许可数

       SemaphoreOnLock(int initialPermits) {
              lock.lock();
              try {
                     permits = initialPermits;
              } finally {
                     lock.unlock();
              }
       }

       //颁发许可，条件是：permits > 0
       public void acquire() throws InterruptedException {
              lock.lock();
              try {
                     while (permits <= 0)//如果没有许可，则等待
                            permitsAvailable.await();
                     --permits;//用一个少一个
              } finally {
                     lock.unlock();
              }
       }

       //归还许可
       public void release() {
              lock.lock();
              try {
                     ++permits;
                     permitsAvailable.signal();
              } finally {
                     lock.unlock();
              }
       }
}
