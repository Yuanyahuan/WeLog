package ShareMaterials;

import BasicKnowledge.MyThread.ManyThreads;

/**
 * @Author: Mike
 * @Description:
 * @Date: Created in 4:41 PM 18/10/2017
 * @Modified By:
 */
public class WildCardTypesClient {

    public static void main(String[] args) {


        testExtend();
        testSuper();
    }

    private static void test0(){

        Employee employee = new Employee();
        Manager manager = new Manager();
        employee = manager;

        Pair<Employee> myEmployees = new Pair<>();
        Pair<Manager> myManagers = new Pair<>();

        //myEmployees = myManagers;

    }

    private static <T extends Comparable> void testGeneric( T some) {

        some.compareTo("");

    }

    private static void testExtend(){

        Pair<Manager> manageBuddies = new Pair<>();
        Pair<? extends Employee> wildBuddies = new Pair<Manager>();
        Employee myEmployee = new Employee();// 父类
        Manager myManager = new Manager(); // 子类

        /**
         *父类转换成子类是不安全的，子类转换成父类是安全的
         */
        manageBuddies.setFirst(myManager);
        /**
         * 为啥这里的Set方法不可以用？然后Get方法是可以用的呢？
         */
        /*
        wildBuddies.setFirst(myEmployee);
        wildBuddies.setFirst(myManager);
        */

        //取出来是没问题的
        Employee mySecondEmployee = wildBuddies.getFirst();

    }

    private static void testSuper(){

        // Employee是父类，Manager是子类
        Pair<Employee> employBuddes = new Pair<>();
        Pair<? super Employee> superTypePair = new Pair<Employee>();

        Employee myEmployee = new Employee();
        Manager myManager = new Manager();

        superTypePair.setFirst(myEmployee);//这里为啥不行？
        superTypePair.setFirst(myManager);

        /**
         * 为啥这里必须使用Object来接
         */
        //Employee mySecondEmployee = superTypePair.getFirst();
        Object mySecondEmployee = superTypePair.getFirst();
    }


}
