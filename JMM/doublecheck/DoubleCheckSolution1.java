package JMMShare.doublecheck;

/**
 * @Author: Mike
 * @Description:
 * @Date: Created in 4:24 PM 11/01/2018
 * @Modified By:
 */
public class DoubleCheckSolution1 {

    private static class ExpensiveObjHolder {
        public static ExpensiveObj instance = new ExpensiveObj();
    }

    public  static ExpensiveObj getInstance() {
        return ExpensiveObjHolder.instance; // 这里将导致ExpensiveObjHolder类被实例化
    }

    /**
     * 根据Java语言规范，以下情况发生时一个类或者接口类型的T被初始化
     *
     * 1. T是一个类，并且T类型的实例被创建
     * 2. T是一个类，并且T中声明的一个静态方法被调用
     * 3. T中声明的一个静态字段被赋值
     * 4. T中声明的一个静态字段被使用，而且这个字段不是一个常量字段
     * 5. T是一个定级类(Top Level Class)，并且一个断言嵌套在T内被执行
     */
}
