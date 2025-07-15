package SortingAlgortithms.insertionsort;
public class InsertionSort {

    // Function to perform Insertion Sort
    public static void insertionSort(int[] arr) {
        int n = arr.length;

        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;

            // Move elements that are greater than key
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }

            // Place the key at its correct location
            arr[j + 1] = key;
        }
    }

    // Main method to test it
    public static void main(String[] args) {
        int[] data = {9, 5, 1, 4, 3};

        System.out.println("Before Insertion Sort:");
        printArray(data);

        insertionSort(data);

        System.out.println("\nAfter Insertion Sort:");
        printArray(data);
    }

    // Helper to print the array
    public static void printArray(int[] arr) {
        for (int value : arr) {
            System.out.print(value + " ");
        }
        System.out.println();
    }
}
