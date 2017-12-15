import java.util.Random;

public class TopHundredChoiceSort {

    public void tophundred(int[] nums, int k) {

        int min, temp, length = nums.length;
        for (int i = 0; i < k; i++) {
            min = i;
            for (int j = i + 1; j < length; j++) {
                if (nums[min] < nums[j]) {
                    min = j;
                }
            }
            temp = nums[i];
            nums[i] = nums[min];
            nums[min] = temp;
        }
    }

    public void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void main(String[] args) {

        // the size of the array  
        int number = 100000000;
        // the top k values  
        int k = 100;
        // the range of the values in the array  
        int range = 1000000001;

        //input for minHeap based method  
        int[] array = new int[number];

        Random random = new Random();
        for (int i = 0; i < number; i++) {
            array[i] = random.nextInt(range);
        }

        TopHundredChoiceSort topHundred = new TopHundredChoiceSort();

        //start time  
        long t1 = System.currentTimeMillis();
        topHundred.tophundred(array, k);
        //end time  
        long t2 = System.currentTimeMillis();

        System.out.println("The total execution time " +
                "of quicksort based method is " + (t2 - t1) +" millisecond!");

        // print out the top k largest values in the top array  
        System.out.println("The top "+ k + "largest values are:");
        for (int i = 0; i < k; i++) {
            System.out.println(array[i]);
        }

    }
}  