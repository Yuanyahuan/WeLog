package ShareMaterials;

import java.util.List;

/**
 * @Author: Mike
 * @Description:
 * @Date: Created in 11:14 AM 18/10/2017
 * @Modified By:
 */
public class ArrayListVSLinkedList {


    public static void main(String[] args) {


    }

    /**
    * 对于ArrayList和LikecList其时间复杂度分别为多少？
    */
    public static void makeList1(List<Integer> lst, int N) {

        lst.clear();
        for (int i = 0; i < N; i++) {

            lst.add(i);
        }
    }

    /**
     * 对于ArrayList和LikedList其时间复杂度分别为多少？
     */
    public static void makeList2(List<Integer> lst, int N) {

        lst.clear();
        for (int i = 0; i < N; i++) {

            lst.add(0,i);
        }
    }

    /**
    * 对于ArrayList和LikedList其时间复杂度分别为多少？
    */
    public static int sum(List<Integer> lst, int N) {

        int total = 0;
        for (int i = 0; i < N; i++) {

            total += lst.get(i);
        }
        return total;
    }

    /**
     * 如果是For each它们的时间复杂度是多少呢？
     */

}
