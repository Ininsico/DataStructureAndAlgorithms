
public class JumpSearch {

    // Function to perform Jump Search
    public static int jumpSearch(int[] arr, int target) {
        int n = arr.length;

        // Optimal block size to be jumped
        int step = (int) Math.floor(Math.sqrt(n));
        int prev = 0;

        // Jump until we find a block where target <= arr[step]
        while (step < n && arr[step] < target) {
            prev = step;
            step += (int) Math.floor(Math.sqrt(n));
        }

        // Do linear search in the found block
        for (int i = prev; i <= Math.min(step, n - 1); i++) {
            if (arr[i] == target) {
                return i;
            }
        }

        return -1; // Not found
    }

    // Main method to test it
    public static void main(String[] args) {
        int[] sortedArray = {3, 7, 12, 18, 25, 31, 42, 56, 64, 77, 89, 95};
        int key = 56;

        int index = jumpSearch(sortedArray, key);

        if (index != -1)
            System.out.println("Element found at index: " + index);
        else
            System.out.println("Element not found.");
    }
}
