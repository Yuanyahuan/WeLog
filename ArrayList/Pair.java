package ShareMaterials;

/**
 * @Author: Mike
 * @Description:
 * @Date: Created in 2:01 PM 18/10/2017
 * @Modified By:
 */
public class Pair <T >{

   private T first;
   private T second;


   Pair(){

       /* 第五条规则，那么如何传入一个类，然后让其自己进行初始化，怎样创建一个泛型的数组？
       first = new T();
       second = new T();
       */

       first = null;
       second = null;
   }

    /**
     *自己实现传入一个类，然后自动将其初始化
     */
   public static <T> Pair<T> makePair(Class<T> cl) {

       try {
           return new Pair<>(cl.newInstance(),cl.newInstance());
       }catch (Exception x) {
           return null;
       }
   }

   Pair(T first, T second){

       this.first = first;
       this.second = second;
   }

    public T getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public void setSecond(T second) {
        this.second = second;
    }




}
