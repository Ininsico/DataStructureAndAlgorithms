//Finding X^n

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter the base number (X):");
        Scanner sc = new Scanner(System.in);
        String x = sc.nextLine();
        System.out.println("Enter the exponent (n):");
        String n = sc.nextLine();
        System.out.println("Result of "+ x + "^" + n + " is: " + power(x, n));
    }
    public static String power(String x,String n){
        for(int i = 0; i < Integer.parseInt(n) - 1; i++){
            x = multiply(x, x);
        }
        return x;
    }
    public static String multiply(String x, String y) {
        int len1 = x.length();
        int len2 = y.length();
        int[] result = new int[len1 + len2];
        
        for (int i = len1 - 1; i >= 0; i--) {
            for (int j = len2 - 1; j >= 0; j--) {
                int mul = (x.charAt(i) - '0') * (y.charAt(j) - '0');
                int sum = mul + result[i + j + 1];
                result[i + j + 1] = sum % 10;
                result[i + j] += sum / 10;
            }
        }
        
        StringBuilder sb = new StringBuilder();
        for (int num : result) {
            if (!(sb.length() == 0 && num == 0)) { // Skip leading zeros
                sb.append(num);
            }
        }
        
        return sb.length() == 0 ? "0" : sb.toString();
    }
}
