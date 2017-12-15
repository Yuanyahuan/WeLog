import java.util.Random;

public class TopHundredQuickSort {

    public void tophundred(int[] array, int start, int end, int k) {

        int switchPointer = start;
        int pivot = array[end]; //array最后一个值作为pivot
        for (int i = start; i < end; i++) {
            if (array[i] >= pivot) {
                swap(array, switchPointer, i);
                switchPointer++;
            }
        }
        swap(array, end, switchPointer);//交换后，array左边的值比pivot大，右边的值比pivot小

        if (switchPointer < k - 1) {
            tophundred(array, switchPointer + 1, end, k);
        } else if (switchPointer == k - 1) {
            return;
        } else {
            tophundred(array, 0, switchPointer - 1, k);
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

        TopHundredQuickSort topHundred = new TopHundredQuickSort();

        //start time  
        long t1 = System.currentTimeMillis();
        topHundred.tophundred(array, 0, array.length - 1, k);
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