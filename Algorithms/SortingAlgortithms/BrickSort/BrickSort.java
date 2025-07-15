package SortingAlgortithms.BrickSort;
public class BrickSort {

    // Function to perform Brick Sort
    public static void brickSort(int[] arr) {
        int n = arr.length;
        boolean sorted = false;

        while (!sorted) {
            sorted = true;

            // Odd-indexed pass
            for (int i = 1; i <= n - 2; i += 2) {
                if (arr[i] > arr[i + 1]) {
                    // Swap
                    int temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    sorted = false;
                }
            }

            // Even-indexed pass
            for (int i = 0; i <= n - 2; i += 2) {
                if (arr[i] > arr[i + 1]) {
                    // Swap
                    int temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    sorted = false;
                }
            }
        }
    }

    // Helper to print the array
    public static void printArray(int[] arr) {
        for (int value : arr) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    // Main method to test it
    public static void main(String[] args) {
        int[] data = {9, 3, 4, 2, 1, 7, 5};

        System.out.println("Before Sorting:");
        printArray(data);

        brickSort(data);

        System.out.println("\nAfter Brick Sort:");
        printArray(data);
    }
}
