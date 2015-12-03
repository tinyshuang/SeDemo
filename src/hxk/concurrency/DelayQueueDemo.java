package hxk.concurrency;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
我们谈一下实际的场景吧。我们在开发中，有如下场景
a) 关闭空闲连接。服务器中，有很多客户端的连接，空闲一段时间之后需要关闭之。
b) 缓存。缓存中的对象，超过了空闲时间，需要从缓存中移出。
c) 任务超时处理。在网络协议滑动窗口请求应答式交互时，处理超时未响应的请求。
d) session超时管理，网络应答通讯协议的请求超时处理
 */

//DelayQueue中的对象只有在其到期时间到了才能被取走,有序队列,

//一个缓存的简单实现。共包括三个类Pair、DelayItem、Cache

class Pair<K, V> {
    public K first;

    public V second;
    
    public Pair() {}
    
    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }
}

//Delayed扩展了Comparable接口，比较的基准为延时的时间值，Delayed接口的实现类getDelay的返回值应为固定值（final）
class DelayItem<T> implements Delayed {
    /** Base of nanosecond timings, to avoid wrapping */
    private static final long NANO_ORIGIN = System.nanoTime();

    /**
     * Returns nanosecond time offset by origin
     * 为了下面的算延迟时间用的:[time - now()]
     */
    final static long now() {
        return System.nanoTime() - NANO_ORIGIN;
    }

    /**
     * Sequence number to break scheduling ties, and in turn to guarantee FIFO order among tied entries.
     */
    private static final AtomicLong sequencer = new AtomicLong(0);

    /** Sequence number to break ties FIFO */
    private final long sequenceNumber;

    /** The time the task is enabled to execute in nanoTime units */
    private final long time;

    private final T item;

    public DelayItem(T submit, long timeout) {
        this.time = now() + timeout;
        this.item = submit;
        this.sequenceNumber = sequencer.getAndIncrement();
    }

    public T getItem() {
        return this.item;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long d = unit.convert(time - now(), TimeUnit.NANOSECONDS);//[time - now()]表示延迟的时间
        //返回延迟的long时间
        return d;
    }

    public int compareTo(Delayed other) {
        if (other == this) // compare zero ONLY if same object
            return 0;
        if (other instanceof DelayItem) {
            DelayItem<?> x = (DelayItem<?>) other;
            long diff = time - x.time;
            if (diff < 0)
                return -1;
            else if (diff > 0)
                return 1;
            else if (sequenceNumber < x.sequenceNumber)
                return -1;
            else
                return 1;
        }
        long d = (getDelay(TimeUnit.NANOSECONDS) - other.getDelay(TimeUnit.NANOSECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }
}


/**
 * @author Administrator
 * @description
 *2015-1-22  上午11:14:09
 */
public class DelayQueueDemo<K, V> {

	    private ConcurrentMap<K, V> cacheObjMap = new ConcurrentHashMap<K, V>();

	    private DelayQueue<DelayItem<Pair<K, V>>> q = new DelayQueue<DelayItem<Pair<K, V>>>();

	    private Thread daemonThread;

	    public DelayQueueDemo() {

	        Runnable daemonTask = new Runnable() {
	            public void run() {
	                daemonCheck();
	            }
	        };

	        daemonThread = new Thread(daemonTask);
	        daemonThread.setDaemon(true);
	        daemonThread.setName("Cache Daemon");
	        daemonThread.start();
	    }

	    private void daemonCheck() {
	        while(!Thread.interrupted()) {
	            try {
	        	//从DelayQueue取回超时对象..若没超时对象,则阻塞
	                DelayItem<Pair<K, V>> delayItem = q.take();
	                //如果有超时对象不为空,删掉
	                if (delayItem != null) {
	                    // 超时对象处理
	                    Pair<K, V> pair = delayItem.getItem();
	                    cacheObjMap.remove(pair.first, pair.second); // compare and remove
	                }
	            } catch (InterruptedException e) {
	                System.out.println("DelayQueue Interrupted");
	            }
	        }

	    }

	    // 添加缓存对象
	    public void put(K key, V value, long time, TimeUnit unit) {
	        V oldValue = cacheObjMap.put(key, value);//添加数据
	        if (oldValue != null)//若不为空则把DelayQueue中旧的删除
	            q.remove(key);

	        long nanoTime = TimeUnit.NANOSECONDS.convert(time, unit);//将缓存的时间转换
	        //往DelayQueue里面存放一个带时间的嵌套Pair
	        q.put(new DelayItem<Pair<K, V>>(new Pair<K, V>(key, value), nanoTime));
	    }

	    public V get(K key) {
	        return cacheObjMap.get(key);
	    }

	    // 测试入口函数
	    public static void main(String[] args) throws Exception {
		DelayQueueDemo<Integer, String> cache = new DelayQueueDemo<Integer, String>();
		//存放一个Map对象<1,"aaaa">..它 的保存时间为3秒
	        cache.put(1, "aaaa", 3, TimeUnit.SECONDS);

	        //睡眠两秒,以后缓存对象是3秒..所以现在还是取得到值得
	        Thread.sleep(1000 * 2);
	        String str = cache.get(1);
	        System.out.println(str);
	        
	        //睡眠4秒后,由于缓存在3秒后消失..所以看不到..	
	        Thread.sleep(1000 * 2);
	        String str2 = cache.get(1);
	        System.out.println(str2);
	        
	    }
}
