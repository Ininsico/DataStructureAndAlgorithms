
public class InterpolationSearch {

    // Interpolation Search function
    public static int interpolationSearch(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high && target >= arr[low] && target <= arr[high]) {
            // Prevent division by zero
            if (arr[low] == arr[high]) {
                if (arr[low] == target)
                    return low;
                else
                    return -1;
            }

            // Estimate the probe position
            int pos = low + ((target - arr[low]) * (high - low)) 
                            / (arr[high] - arr[low]);

            // Check if found
            if (arr[pos] == target) {
                return pos;
            }
            // If target is larger, search right
            else if (arr[pos] < target) {
                low = pos + 1;
            }
            // If target is smaller, search left
            else {
                high = pos - 1;
            }
        }

        return -1; // Not found
    }

    // Main method to test it
    public static void main(String[] args) {
        int[] data = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        int key = 70;

        int result = interpolationSearch(data, key);

        if (result != -1)
            System.out.println("Element found at index: " + result);
        else
            System.out.println("Element not found.");
    }
}

