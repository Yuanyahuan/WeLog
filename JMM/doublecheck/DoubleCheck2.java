package JMMShare.doublecheck;

/**
 * @Author: Mike
 * @Description:
 * @Date: Created in 4:21 PM 11/01/2018
 * @Modified By:
 */
public class DoubleCheck2 {

    private static ExpensiveObj instance;

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
