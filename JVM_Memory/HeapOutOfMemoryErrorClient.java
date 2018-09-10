package com.william.abner.jvm.jvmmemoryerror;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqi05 on 2017/12/1.
 * 堆的OutOfMemoryError错误
 */
public class HeapOutOfMemoryErrorClient {
    static class OOMObject{
    }

    /**
     * -verbose:gc -Xms20m -Xmx20m -Xmn10m -XX:+PrintGCDetails -XX:SurvivorRatio=8
     * -XX:+HeapDumpOnOutOfMemoryError
     */
    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<OOMObject>();
        while (true){
            list.add(new OOMObject());
        }
    }
}
