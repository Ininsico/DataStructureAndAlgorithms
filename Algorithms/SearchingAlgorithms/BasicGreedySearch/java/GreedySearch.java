import java.util.*;

public class GreedySearch {
    public static List<Integer> greedySearch(int[] coins, int amount) {
        Arrays.sort(coins);
        reverseArray(coins);
        List<Integer> result = new ArrayList<>();
        for (int coin : coins) {
            while (amount >= coin) {
                result.add(coin);
                amount -= coin;
            }
        }
        if (amount != 0) {
            System.out.println("Cannot make change for the given amount with the provided coins.");
            return new ArrayList<>();
        }
        return result;
    }

    private static void reverseArray(int[] arr) {
        int left = 0, right = arr.length - 1;
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }

    public static void main(String[] args) {
        int[] coins = { 1, 5, 10, 25 };
        int amount = 63;
        List<Integer> result = greedySearch(coins, amount);
        if (!result.isEmpty()) {
            System.out.println("Coins used to make change: " + result);
        } else {
            System.out.println("No solution found.");
        }
    }
}

