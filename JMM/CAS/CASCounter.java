package JMMShare.CAS;

/**
 * @Author: Mike
 * @Description:
 * @Date: Created in 3:58 PM 11/01/2018
 * @Modified By:
 */
public class CASCounter {
    private SimulatedCAS value;

    public int getValue() {
        return value.get();
    }
    public int increment() {

        int v;
        do {
            v = value.get();
        } while (v != value.compareAndSwap(v,v+1));

        return v + 1;
    }
}
