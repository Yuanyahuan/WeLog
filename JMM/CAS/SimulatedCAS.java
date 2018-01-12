package JMMShare.CAS;

/**
 * @Author: Mike
 * @Description:
 * @Date: Created in 3:54 PM 11/01/2018
 * @Modified By:
 */
public class SimulatedCAS {
    private int value;

    public synchronized int get() {
        return value;
    }

    public synchronized int compareAndSwap(int expetedValue, int newValue) {

        int oldValue = value;
        if (oldValue == expetedValue) {
            value = newValue;
        }
        return oldValue;
    }

    public synchronized boolean compareAndSet(int expectedValue, int newValue) {
        return (expectedValue == compareAndSwap(expectedValue,newValue));

    }
}
