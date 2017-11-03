package com.zry.demo;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by yun on 2017/10/23.
 */
public class ArrayDequeAndLinkedList {
    public static void main(String[] args) throws Exception{
        int time = 10000000;

        long s1 = System.currentTimeMillis();
        Queue<Integer> queue1 = new ArrayDeque<Integer>(time);

        for (int i = 0; i < time; i++) {
           // String s = i+",";
            queue1.offer(i);
        }
        for (int i = 0; i < time; i++) {
            queue1.poll();
        }
        System.out.println("ArrayDeque cost time:" + (System.currentTimeMillis() - s1));

        long s2 = System.currentTimeMillis();
        Queue<Integer> queue2 = new LinkedList<Integer>();
        for (int i = 0; i < time; i++) {
            queue2.offer(i);
        }
        for (int i = 0; i < time; i++) {
            queue2.poll();
        }
        System.out.println("LinkedList cost time:" + (System.currentTimeMillis() - s2));
    }
}
