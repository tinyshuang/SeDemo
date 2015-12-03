package hxk.collection;

import java.util.LinkedList;

/**
 * @author Administrator
 * @description 先进先出
 *2015-1-27  上午11:36:22
 */
public class Queue<T>{
    private java.util.Queue<T> list = new LinkedList<T>();
    
    public boolean offer(T e){
	return list.offer(e);
    }
    
    public T peek(){
	return list.peek();
    }
    
    public T poll(){
	return list.poll();
    }
    
    public T remove(){
	return list.remove();
    }
    
    public boolean isEmpty(){
	return list.isEmpty();
    }
    
    public String toString(){
	return list.toString();
    }
}
