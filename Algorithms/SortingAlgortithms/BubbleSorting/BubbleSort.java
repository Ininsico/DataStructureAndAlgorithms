package SortingAlgortithms.BubbleSorting;

public class BubbleSort {

    // Bubble Sort Function
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        boolean swapped;

        // Outer loop for passes
        for (int i = 0; i < n - 1; i++) {
            swapped = false;

            // Inner loop for pairwise comparison
            for (int j = 0; j < n - i - 1; j++) {
                // Swap if elements are in wrong order
                if (arr[j] > arr[j + 1]) {
                    // Swap arr[j] and arr[j + 1]
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;

                    swapped = true;
                }
            }

            // If no two elements were swapped, array is already sorted
            if (!swapped)
                break;
        }
    }

    // Main method to test it
    public static void main(String[] args) {
        int[] data = {64, 34, 25, 12, 22, 11, 90};

        System.out.println("Before Sorting:");
        printArray(data);

        bubbleSort(data);

        System.out.println("\nAfter Bubble Sort:");
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
