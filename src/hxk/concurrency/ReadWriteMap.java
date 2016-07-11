/**
 * 当你只需要一个并发的基于散列的映射,那么ConcurrentHashMap就行..
 * 如果你需要Map的另一种实现(比如LinkedHashMap),那么可以使用读写锁..
 */
public class ReadWriteMap<K, V> {
    private final Map<K, V> map;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock r = lock.readLock();
    private final Lock w = lock.writeLock();
    
    public ReadWriteMap(Map<K, V> map){
        this.map = map;
    }
    
    // 其他写操作...
    
    public V put(K key, V value){
        w.lock(); //请求写锁
        try{
            return map.put(key, value);
        } finally{
            w.unlock(); //勿忘
        }
    }
    
    // 其他读操作...
    
    public V get(K key){
        r.lock(); //请求读锁
        try{
            return map.get(key);
        } finally{
            r.unlock(); //勿忘
        }
    }
}
