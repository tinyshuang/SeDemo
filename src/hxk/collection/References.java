package hxk.collection;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * @author Administrator
 * @description 介绍Java的引用..
 *2015-1-28  下午2:47:16
 */

class VeryBig{
    private static final int SIZE  = 10000;
    @SuppressWarnings("unused")
    private long[] la = new long[SIZE];
    private String ident;
    public VeryBig(String id){
	this.ident = id;
    }
    
    @Override
    public String toString() {
	return ident;
    }
    
    protected void finalize(){
	System.out.println("Finalize " + ident);
    }
    
}

public class References {
    private static ReferenceQueue<VeryBig> rq = new ReferenceQueue<VeryBig>();
    
    public static void checkQueue(){
	Reference<? extends VeryBig> inq = rq.poll();
	if(inq!=null)
	    System.out.println("In queue " + inq.get());
    }
    
    public static void main(String[] args) {
	int size = 10;
	LinkedList<SoftReference<VeryBig>> sa = new LinkedList<SoftReference<VeryBig>>();
	
	for (int i = 0; i < size; i++) {
	    sa.add(new SoftReference<VeryBig>(new VeryBig("Soft " + i),rq));
	    System.out.println("Just Created " + sa.getLast());
	    checkQueue();
	}
	
	System.out.println("-------------------sa---------------------");
	
	LinkedList<WeakReference<VeryBig>> wa = new LinkedList<WeakReference<VeryBig>>();
	for (int i = 0; i < size; i++) {
	    wa.add(new WeakReference<VeryBig>(new VeryBig("Weak " + i),rq));
	    System.out.println("Just Created " + wa.getLast());
	    checkQueue();
	}
	
	System.out.println("--------------------wa--------------------");
	
	@SuppressWarnings("unused")
	SoftReference<VeryBig> s = new SoftReference<VeryBig>(new VeryBig("Soft"));
	@SuppressWarnings("unused")
	WeakReference<VeryBig> w = new WeakReference<VeryBig>(new VeryBig("Weak"));
	System.gc();
	
	LinkedList<PhantomReference<VeryBig>> pa = new LinkedList<PhantomReference<VeryBig>>();
	for (int i = 0; i < size; i++) {
	    pa.add(new PhantomReference<VeryBig>(new VeryBig("Phantom " + i),rq));
	    System.out.println("Just Created " + pa.getLast());
	    checkQueue();
	}
	System.out.println("------------------pa----------------------");
	
    }
}
