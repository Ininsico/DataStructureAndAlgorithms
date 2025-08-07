
//Given an array of integars return the Amount of iterations needed to make all the elements equal 
//u can only increment of decrement the elements by 1 in each iteration
// Example: [1, 2, 3] -> 2 iterations (1+1, 2-1, 3-1) -> [2, 2, 2]
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner Scanner = new Scanner(System.in);
        System.out.println("Enter the number of elements in the array:");
        int choice = Scanner.nextInt();
        int[] arr = new int[choice];
        System.out.println("Enter the elements of the array:");
        for (int i = 0; i < choice; i++) {
            arr[i] = Scanner.nextInt();
        }
        System.out.println("The number of iterations needed to make all elements equal is: " + findIterations(arr));
    }

    public static int findIterations(int[] arr) {
        int max = arr[0];
        int min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return max - min;
    }
}