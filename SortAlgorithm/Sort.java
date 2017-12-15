/**
 * Created by root1 on 2017/12/4.
 */
public class Sort {
    public static void selectSort(int[] nums) {
        int min, temp, length = nums.length;
        for (int i = 0; i < length; i++) {
            min = i;
            for (int j = i + 1; j < length; j++) {
                if (nums[min] > nums[j]) {
                    min = j;
                }
            }
            temp = nums[i];
            nums[i] = nums[min];
            nums[min] = temp;
        }
    }

    public static void maxHeapDown(int[] nums, int head, int tail) {
        int p = head, l = 2*p + 1, r = l + 1;
        int tmp = nums[p];
        for (; l <= tail; p = l,l = 2*l + 1,r = l + 1) {
            if (l < tail && nums[l] < nums[r]) {
                l = r;
            }
            if (tmp >= nums[l]) {
                break;
            } else {
                nums[p] = nums[l];
                nums[l]= tmp;
            }
        }
    }

    public static void heapSort(int[] nums) {
        int i, tmp, length = nums.length;
        for (i = length/2 - 1; i >= 0; i--) {
            maxHeapDown(nums, i, length - 1);
        }
        for (i = length - 1; i > 0; i--) {
            tmp = nums[0];
            nums[0] = nums[i];
            nums[i] = tmp;
            maxHeapDown(nums, 0, i - 1);
        }
    }

    public static void insertSort(int[] nums) {
        int temp, length = nums.length;
        for (int i = 1; i < length; i++) {
            temp = nums[i];
            int j = i;
            for (; j >= 1&&temp < nums[j - 1]; j--) {
                nums [j] = nums[j - 1];
            }
            nums[j] = temp;
        }
    }

    public static void shellSort(int[] nums) {
        int temp, length = nums.length;
        for (int gap = length/2; gap > 0; gap /= 2) {
            for (int i = 0; i < gap ; i++) {
                for (int j = i + gap; j < length; j += gap) {
                    temp = nums[j];
                    int k = j;
                    for (; k >= gap&&temp < nums[k - gap]; k -= gap) {
                        nums[k] = nums[k - gap];
                    }
                    nums[k] = temp;
                }
            }
        }
    }

    public static void bubbleSort(int[] nums) {
        int length = nums.length;
        for (int i = 0; i < length; i++) {
            for (int j = length - 1; j > i; j--) {
                if (nums[j - 1] > nums[j]) {
                    int temp = nums[j - 1];
                    nums[j - 1] = nums[j];
                    nums[j] = temp;
                }
            }
        }
    }

    public static void recursiveQuickSort(int[] nums, int head, int tail) {
        int i = head, j = tail;
        int pivot = nums[head];
        while (i < j) {
            while (i < j) {
                if (pivot >= nums[j]) {
                    nums[i++] = nums[j];
                    break;
                }
                j--;
            }
            while (i < j) {
                if (pivot <= nums[i]) {
                    nums[j--] = nums[i];
                    break;
                }
                i++;
            }

        }
        nums[i] = pivot;
        if (i - 1 - head > 0) {
            recursiveQuickSort(nums, head, i - 1);
        }
        if (tail - i - 1 > 0) {
            recursiveQuickSort(nums, i + 1, tail);
        }
    }

    public static void merge(int[] nums, int head, int median, int tail) {
        int[] nums1 = new int[median - head + 1];
        int[] nums2 = new int[tail - median];
        int length1 = nums1.length, length2 = nums2.length;
        System.arraycopy(nums, head, nums1, 0, length1);
        System.arraycopy(nums, median + 1, nums2, 0, length2);
        int i = 0, j = 0, k = head;
        while (i < length1&&j < length2) {
            nums[k++] = (nums1[i] < nums2[j])?nums1[i++]:nums2[j++];
        }
        while (i < length1) {
            nums[k++] = nums1[i++];
        }
        while (j < length2) {
            nums[k++] = nums2[j++];
        }
    }

    public static void recursiveMergeSort(int[] nums, int head, int tail) {
        int median = (head + tail)/2;
        if (median != tail) {
            recursiveMergeSort(nums, head, median);
            recursiveMergeSort(nums, median + 1, tail);
        }
        merge(nums, head, median, tail);
    }



}
