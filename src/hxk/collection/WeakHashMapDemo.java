package hxk.collection;

import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @description WeakHashMap的Demo....
 * 触发WeakHashMap的条件是:不再需要此键了..
 *2015-1-28  下午4:53:35
 */

class Element{
    private String ident;

    public Element(String ident) {
	super();
	this.ident = ident;
    }

    @Override
    public String toString() {
	return ident;
    }

    @Override
    public int hashCode() {
	return ident.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Element other = (Element) obj;
	if (ident == null) {
	    if (other.ident != null)
		return false;
	} else if (!ident.equals(other.ident))
	    return false;
	return true;
    }
    
    protected void finalize(){
	System.out.println("Finalize " + getClass().getSimpleName() +" " + ident);
    }
}

class Key extends Element{
    public Key(String ident) {
	super(ident);
    }
}

class Value extends Element{
    public Value(String ident) {
	super(ident);
    }
    
}
public class WeakHashMapDemo {
    public static void main(String[] args) throws InterruptedException {
	int size = 100;
	Key[] keys= new Key[size];
	WeakHashMap<Key, Value> map = new WeakHashMap<Key, Value>();
	for (int i = 0; i < size; i++) {
	    Key key = new Key(Integer.toString(i));
	    Value value = new Value(Integer.toString(i));
	    if (i%3 == 0) 
		keys[i] = key;//save as "real" reference
	    map.put(key, value);
	}
	System.gc();
	TimeUnit.SECONDS.sleep(5);
    }
}
