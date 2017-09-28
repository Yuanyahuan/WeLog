package com.william.abner.javacollections.guava;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Created by zhangqi05 on 2017/9/20.
 * 支持正反向映射，可以通过key找到value，也可以通过value找到key
 */
public class BiMapClient {
    public static void main(String args[]){
        BiMap<Integer, String> empIDNameMap = HashBiMap.create();

        empIDNameMap.put(new Integer(101), "Mahesh");
        empIDNameMap.put(new Integer(102), "Sohan");
        empIDNameMap.put(new Integer(103), "Ramesh");

        //Emp Id of Employee "Mahesh"
        System.out.println(empIDNameMap.get(101));
        System.out.println(empIDNameMap.inverse().get("Mahesh"));
    }
}
