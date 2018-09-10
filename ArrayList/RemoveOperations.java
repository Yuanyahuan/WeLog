package ShareMaterials;

import com.sun.javafx.scene.control.skin.VirtualFlow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: Mike
 * @Description:
 * @Date: Created in 11:26 AM 18/10/2017
 * @Modified By:
 */
public class RemoveOperations {

    public static void main(String[] args) {

        ArrayList searchedArrayList = new ArrayList();
        for (int i = 0; i < 800000; i++) {

            searchedArrayList.add(i);

        }

        LinkedList searchedLinkedList = new LinkedList();

        for (int i = 0; i < 800000; i++) {

            searchedLinkedList.add(i);
        }

        long begin0 = System.currentTimeMillis();

        removeEventVer3(searchedArrayList);

        long end0 = System.currentTimeMillis();

        System.out.println("ArrayList Cost:----" + (end0 - begin0));

        long begin1 = System.currentTimeMillis();

        removeEventVer3(searchedLinkedList);

        long end1 = System.currentTimeMillis();

        System.out.println("LinkedList Cost:---" + (end1 - begin1));

    }

    /**
     *方法1：对于ArrayList和LikedList的时间复杂度分别是多少？
     */
       public static void removeEvensVer1(List<Integer> lst) {

        int i = 0;

        while (i < lst.size()) {

            if (lst.get(i) % 2 == 0) {

                lst.remove(i);
            }else {

                i ++;
            }
        }
    }

    /**
     * 方法2:这种方法可行吗？消耗性能大吗？时间复杂度是多少？
     * @param list
     */
    public static void removeEventVer2(List<Integer> list) {

        for (Integer x: list) {

            if (x % 2 == 0) {
                list.remove(x);
            }
        }
    }

    /**
     * 方法3:这种方法可行吗？其对ArrayList和LinkedList的时间复杂度分别是多少？
     */
    public static void removeEventVer3(List<Integer> list) {

        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {

            if (iterator.next() % 2 == 0){

                iterator.remove();
            }
        }
    }





}
