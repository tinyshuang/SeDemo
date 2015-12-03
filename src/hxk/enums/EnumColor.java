package hxk.enums;

/**
 * @author Administrator
 * @description
 *2015-1-29  上午10:26:30
 */
public enum EnumColor {
    RED("红色"),
    WHITE("白色"),
    BLACK("黑色");
    
    //必须先定义枚举再定义其他属性和方法..
    
    private String description;//使用自定义的属性..让每个枚举实例都能获取这个对应的属性

    private EnumColor(String description) {
	this.description = description;
    }
    
    public String getDescription(){
	return description;
    }
    
    public static void main(String[] args) {
	for (EnumColor color : EnumColor.values()) {
	    System.out.println(color +"---"+ color.getDescription());
	}
    }
    
}
