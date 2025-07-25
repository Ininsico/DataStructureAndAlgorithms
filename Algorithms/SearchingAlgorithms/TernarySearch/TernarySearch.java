
public class TernarySearch {

    // Recursive Ternary Search
    public static int ternarySearch(int[] arr, int low, int high, int target) {
        if (low > high) {
            return -1; // Not found
        }

        // Divide range into three parts
        int mid1 = low + (high - low) / 3;
        int mid2 = high - (high - low) / 3;

        // Check mid1 and mid2
        if (arr[mid1] == target) return mid1;
        if (arr[mid2] == target) return mid2;

        if (target < arr[mid1]) {
            // Search in the first third
            return ternarySearch(arr, low, mid1 - 1, target);
        } else if (target > arr[mid2]) {
            // Search in the third third
            return ternarySearch(arr, mid2 + 1, high, target);
        } else {
            // Search in the middle third
            return ternarySearch(arr, mid1 + 1, mid2 - 1, target);
        }
    }

    // Main method to test it
    public static void main(String[] args) {
        int[] sortedArray = {2, 6, 12, 18, 26, 35, 49, 53, 67, 80, 95};
        int key = 35;

        int index = ternarySearch(sortedArray, 0, sortedArray.length - 1, key);

        if (index != -1)
            System.out.println("Element found at index: " + index);
        else
            System.out.println("Element not found.");
    }
}
