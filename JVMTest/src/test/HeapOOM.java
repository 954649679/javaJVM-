package test;

import java.util.ArrayList;
import java.util.List;

/**
 * 堆异常演示
 *
 * VM Args: -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 * 通过VM参数Xms和Xmx设置虚拟机可用内存大小来演示堆内存溢出
 * ps:启动时务必需要设置VM内存
 */
public class HeapOOM {
    static class OOMObject {
    }

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        while (true) {
            list.add(new OOMObject());
        }
    }
}
