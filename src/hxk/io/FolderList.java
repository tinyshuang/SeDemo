package hxk.io;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * 
 * @author Administrator
 * @description 文件夹列出文件时过滤文件
 *2015-1-23  下午4:02:57
 */

class Filter implements FilenameFilter{
    private Pattern pattern;

    public Filter(String name) {
	pattern = Pattern.compile(".*"+name+".*");
    }


    @Override
    public boolean accept(File dir, String name) {
	return pattern.matcher(name).matches();
    }
    
}

/**
 * @author Administrator
 * @description
 *2015-1-23  下午3:49:57
 */
public class FolderList {
    public static void main(String[] args) {
	File file = new File("d://");
	if(file.isDirectory()){
	    String[] files = file.list(new Filter("iso"));
	    for (String name : files) {
		System.out.println(name);
	    }
	    
	}
	else {
	    System.out.println(file.getName());
	}
	    
    }
}
