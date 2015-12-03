package hxk.collection;

import java.util.LinkedList;

/**
 * @author Administrator
 * @description 后进先出
 * 全部操作都是针对第一个值操作..就能做到后进先出
 *2015-1-27  上午11:08:45
 */
public class Stack<T> {
    private LinkedList<T> list = new LinkedList<T>();
    
    public void push(T e){
	list.addFirst(e);
    }
    
    public T pop(){
	return list.removeFirst();
    }
    
    public T peek(){
	return list.getFirst();
    }
    
    public boolean isEmpty(){
	return list.isEmpty();
    }
    
    public String toString(){
	return list.toString();
    }
    
}
