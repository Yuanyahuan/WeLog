package ShareMaterials;

import com.sun.javafx.scene.control.skin.VirtualFlow;

import javax.swing.*;
import java.lang.reflect.Array;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * @Author: Mike
 * @Description:
 * @Date: Created in 2:05 PM 18/10/2017
 * @Modified By:
 */
public class PairClient {

    public static void main(String[] args) {

        testOne();
        testTwo();
        testThree();
        testFour();
    }

    /**
     * 泛型不能使用基本数据类型进行初始化
     */
    private static void testOne(){

        Pair<Double> pair = new Pair<>();
//        Pair<double> pair1 = new Pair<double>;
    }

    /**
     * 泛型不能用在runtime时期的类型检测
     */
    private  static void testTwo(){

        Pair<String> stringPair = new Pair<>();
        Pair<Double> doublePair = new Pair<>();
        /*
        if (stringPair instanceof Pair<String>){

            //....
        }

        /*
        if (stringPair instanceof Pair<T>){
           //...

        }
        */

        if (stringPair.getClass() == doublePair.getClass()){

            System.out.println("StringPair and DoublePair is equal");

        }else {

            System.out.println("------");
        }

    }

    /**
     *
     */
    private  static void testThree(){

        /**
         * 不能创建参数类型的数组，You cannnot creat arrays of parameterized types.
         */
        String[] myTables = new String[10];

        //Pair<String>[] tables = new Pair<String>()[10];

        /**
         * 为啥会这样呢？
         */
        Pair[] table1 = null;
        Object[] objects = table1;
        objects[0] = "Hi,there!";
        objects[1] = new Pair<Double>();

        /**
         * 怎么办？
         */
        Pair<String>[] validTable = (Pair<String>[]) new Pair<?>[10];

        /**
         * 这样做好吗？正确的做法是什么？
         */
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    private static void testFour(){

        Collection<Pair<String>> table = null;
        Pair<String> pair1 = null;
        Pair<String> pair2 = null;
        addAll(table,pair1,pair2);

    }

    private static <T> void addAll(Collection<T> collection, T... ts){

        for(T t:ts) {
            collection.add(t);
        }
    }

    private static void testFive(){

        /**
         * 见Pari类
         */

        minMax("Tom","Jack","Amy","Jimme");
    }

    /*
    public static <T extends Comparable> T[] minMax(T... a) {

        T[] mm = new T[2];
        return mm;
    }
    */

    public static <T extends  Comparable> T[] minMax(T... a) {

        Object[] mm = new Object[2];
        return (T[]) mm;
    }

    public static <T extends  Comparable> T[] minMax2(T... a) {

        T[] mm = (T[])Array.newInstance(a.getClass().getComponentType(),2);
        return mm;
    }

    private static void testSix(){

        /**
         * 见Singleton类
         */
    }

//
//    private static<T extends Throwable> void testSeven(Class<T> t){
//
//        try {
//
//        }catch (T e){
//
//            System.out.println(e);
//        }
//    }


    private static <T extends Throwable> void testSeven1(T t) throws T{

        try {

        }
        catch (Throwable reason) {

            t.initCause(reason);
            throw t;

        }

    }


    private static void testEight() {

        /**
         * 见Pair类
         */
    }






}
