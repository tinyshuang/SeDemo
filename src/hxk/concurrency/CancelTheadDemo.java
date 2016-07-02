 /**
任务取消
  如果外部代码能够在某个操作正常完成之前将其置于 完成 状态，那么这个操作就可以称为可取消的Cancellable
其中一种协作机制是设置一个取消标志Cancellation Requested标志，而任务定期查看该标志。
*/
public class PrimeGenerator implements Runnable {  
    private static ExecutorService exec = Executors.newCachedThreadPool();  
  
    @GuardedBy("this")  
    private final List<BigInteger> primes = new ArrayList<BigInteger>();  
    private volatile boolean cancelled;  
  
    public void run() {  
        BigInteger p = BigInteger.ONE;  
        while (!cancelled) {  
            p = p.nextProbablePrime();  
            synchronized (this) {  
                primes.add(p);  
            }  
        }  
    }  
  
    public void cancel() {  
        cancelled = true;  
    }  
  
    public synchronized List<BigInteger> get() {  
        return new ArrayList<BigInteger>(primes);  
    }  
  
    static List<BigInteger> aSecondOfPrimes() throws InterruptedException {  
        PrimeGenerator generator = new PrimeGenerator();  
        exec.execute(generator);  
        try {  
            SECONDS.sleep(1);  
        } finally {  
            generator.cancel();  
        }  
        return generator.get();  
    }  
}  
