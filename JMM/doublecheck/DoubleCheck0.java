package JMMShare.doublecheck;

/**
 * @Author: Mike
 * @Description:
 * @Date: Created in 4:03 PM 11/01/2018
 * @Modified By:
 */
public class DoubleCheck0 {

    Thread thread = new Thread();


    private static ExpensiveObj instance;
    public static ExpensiveObj getInstance() {

        if (instance == null) {
            instance = new ExpensiveObj();
        }
        return instance;
    }

}
