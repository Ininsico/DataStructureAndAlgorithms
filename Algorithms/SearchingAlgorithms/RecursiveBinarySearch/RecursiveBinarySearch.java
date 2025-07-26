package RecursiveBinarySearch;

public class RecursiveBinarySearch {

    // Recursive Binary Search function
    public static int binarySearch(int[] arr, int low, int high, int target) {
        if (low > high) {
            return -1; // Base case: not found
        }

        int mid = low + (high - low) / 2;

        if (arr[mid] == target)
            return mid;
        else if (arr[mid] < target)
            return binarySearch(arr, mid + 1, high, target); // Search right half
        else
            return binarySearch(arr, low, mid - 1, target);  // Search left half
    }

    // Main method to test
    public static void main(String[] args) {
        int[] sortedArray = {3, 8, 15, 23, 42, 56, 67, 78, 91, 105};
        int key = 42;

        int index = binarySearch(sortedArray, 0, sortedArray.length - 1, key);

        if (index != -1)
            System.out.println("Element found at index: " + index);
        else
            System.out.println("Element not found.");
    }
}
