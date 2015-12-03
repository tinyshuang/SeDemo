package hxk.enums;

/**
 * @author Administrator
 * @description
 *2015-1-29  上午10:16:55
 */

enum Color{RED,YELLOW,BLACK,WHITE}

public class BasicEnum {
    public static void main(String[] args) {
	for (Color color : Color.values()) {//values方法返回枚举实例数组
	    System.out.println("输出枚举的序号: " + color.ordinal());//ordinal方法返回枚举的序号
	    System.out.println("输出枚举的名字 : " + color.name());//返回枚举实例的名字
	    System.out.println("------------------------------");
	}
    }
}
