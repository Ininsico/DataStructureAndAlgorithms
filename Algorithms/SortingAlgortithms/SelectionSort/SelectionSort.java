package SortingAlgortithms.SelectionSort;
public class SelectionSort {

    // Selection Sort Function
    public static void selectionSort(int[] arr) {
        int n = arr.length;

        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in unsorted array
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            // Swap the found minimum element with the first element
            int temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;
        }
    }

    // Main method to test it
    public static void main(String[] args) {
        int[] data = {29, 10, 14, 37, 13};

        System.out.println("Before Sorting:");
        printArray(data);

        selectionSort(data);

        System.out.println("\nAfter Selection Sort:");
        printArray(data);
    }

    // Utility function to print array
    public static void printArray(int[] arr) {
        for (int value : arr) {
            System.out.print(value + " ");
        }
        System.out.println();
    }
}

