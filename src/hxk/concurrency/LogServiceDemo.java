/**
* 通过调用log方法将日志消息放入某个队列中，并由其他线程来处理
* 通过原子操作来检查关闭请求,并使用一个计数量来保证put进去的日志在关闭前能充分take出来..
*/
public class LogService {  
    private final BlockingQueue<String> queue;  
    private final LoggerThread loggerThread;  
    private final PrintWriter writer;  
    @GuardedBy("this")  
    private boolean isShutdown;  
    @GuardedBy("this")  
    private int reservations;  
  
    public LogService(Writer writer) {  
        this.queue = new LinkedBlockingQueue<String>();  
        this.loggerThread = new LoggerThread();  
        this.writer = new PrintWriter(writer);  
    }  
  
    public void start() {  
        loggerThread.start();  
    }  
  
    public void stop() {  
        synchronized (this) {  
            isShutdown = true;  
        }  
        loggerThread.interrupt();  
    }  
  
    public void log(String msg) throws InterruptedException {  
        synchronized (this) {  
            if (isShutdown)  
                throw new IllegalStateException(/*...*/);  
            ++reservations;  
        }  
        queue.put(msg);  
    }  
  
    private class LoggerThread extends Thread {  
        public void run() {  
            try {  
                while (true) {  
                    try {  
                        synchronized (LogService.this) {  
                            if (isShutdown && reservations == 0)  
                                break;  
                        }  
                        String msg = queue.take();  
                        synchronized (LogService.this) {  
                            --reservations;  
                        }  
                        writer.println(msg);  
                    } catch (InterruptedException e) { /* retry */  
                    }  
                }  
            } finally {  
                writer.close();  
            }  
        }  
    }  
}  
