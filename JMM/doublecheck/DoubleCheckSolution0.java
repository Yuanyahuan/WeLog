package JMMShare.doublecheck;

/**
 * @Author: Mike
 * @Description:
 * @Date: Created in 4:24 PM 11/01/2018
 * @Modified By:
 */
public class DoubleCheckSolution0 {

    private volatile static ExpensiveObj instance;
    public  static ExpensiveObj getInstance() {

        if (instance == null) {
            synchronized (DoubleCheck2.class) {

                if (instance == null) {
                    instance = new ExpensiveObj();
                }
            }
        }
        return instance;
    }
}
