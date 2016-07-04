import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;


/**
*使用信号量来控制任务的提交速率
*/
public class BounedExecutor {
    private final Executor exec;
    //通过信号量来限制任务的到达率
    private final Semaphore semaphore;
 
    public BounedExecutor(Executor exec, int bound) {
        super();
        this.exec = exec;
        this.semaphore = new Semaphore(bound);
    }
 
    public void submitTask(final Runnable command) throws InterruptedException {
        semaphore.acquire();
        try {
            exec.execute(new Runnable() {
 
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        System.out.println("command.submit;");
                        command.run();
                    } finally {
                        semaphore.release();
                    }
 
                }
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
        }
 
    }
 
    public static void main(String[] args) {
        BounedExecutor be = new BounedExecutor(Executors.newFixedThreadPool(3),
                3);
        for (int i = 0; i < 6; i++) {
            submittask(be);
        }
 
    }
 
    private static void submittask(BounedExecutor be) {
        // TODO Auto-generated method stub
        try {
            be.submitTask(new Runnable() {
 
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    while (true) {
                        System.out.println(Thread.currentThread().getName());
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
