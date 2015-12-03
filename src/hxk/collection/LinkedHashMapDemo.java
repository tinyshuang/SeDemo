package hxk.collection;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Administrator
 * @description 使用基于LinkedHashMap的最近最少使用(LRU)Map..
 * 结果为:
 * 
 * {0=0, 1=1, 2=2, 3=3, 4=4, 5=5, 6=6, 7=7, 8=8, 9=9}
    After Visit some Map
   {5=5, 6=6, 7=7, 8=8, 9=9, 0=0, 1=1, 2=2, 3=3, 4=4}
 *2015-1-27  下午5:27:27
 */
public class LinkedHashMapDemo {
    private static void initMap(Map<Integer, String> map){
	for (int i = 0; i < 10; i++) 
	    map.put(i, i+"");
    }
    
    private static void visitMap(Map<Integer, String> map){
	for (int i = 0; i < 5; i++) 
	    map.get(i);
    }
    
    
    
    public static void main(String[] args) {
	LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer,String>(16,0.75f,true);
	initMap(map);
	System.out.println(map);
	visitMap(map);
	System.out.println("After Visit some Map");
	System.out.println(map);
    }
}
