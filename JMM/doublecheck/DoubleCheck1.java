package JMMShare.doublecheck;

/**
 * @Author: Mike
 * @Description:
 * @Date: Created in 4:11 PM 11/01/2018
 * @Modified By:
 */
public class DoubleCheck1 {

    private static ExpensiveObj instance;

    public synchronized static ExpensiveObj getInstance() {
        if (instance == null) {
            instance = new ExpensiveObj();
        }
        return instance;
    }
}
