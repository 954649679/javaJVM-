package test;

import java.util.ArrayList;
import java.util.List;

/**
 * 常量池异常演示
 * <p>
 * VM -args -XX:PermSize10m 和 -XX:MaxPermSize10设置常量池的内存大小
 */
public class RunTimeConstantPoolOOM {
    public static void main(String[] args) {
        //使用list保持常量池的引用避免GC回收常量池
        List<String> list = new ArrayList<>();
        int i = 0;
        while (true) {
            //String.intern()的作用：如果字符常量池中已经存在一个等于次String对象的
            //字符串，则返回代表池中的字符串的String对象；否则将次String对象包含的字
            // 符串添加到常量池中，并且返回次String对象的引用
            list.add(String.valueOf(i++).intern());
        }
    }
}
