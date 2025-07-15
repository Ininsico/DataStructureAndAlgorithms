package SortingAlgortithms.Quicksort;

public class QuickSort {

    // Function to perform Quick Sort
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            // Partition the array and get the pivot index
            int pivotIndex = partition(arr, low, high);

            // Recursively sort elements before and after partition
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    // Partition function
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high]; // choosing last element as pivot
        int i = low - 1; // index of smaller element

        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                // swap arr[i] and arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // swap arr[i+1] and arr[high] (pivot)
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    // Main method to test it
    public static void main(String[] args) {
        int[] data = {34, 7, 23, 32, 5, 62};

        System.out.println("Before Quick Sort:");
        printArray(data);

        quickSort(data, 0, data.length - 1);

        System.out.println("\nAfter Quick Sort:");
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
