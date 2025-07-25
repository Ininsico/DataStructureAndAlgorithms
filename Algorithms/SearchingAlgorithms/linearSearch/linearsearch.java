
public class linearsearch {
    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }
    public static void main(String[] args) {
        int[] data = {1,2,3,4,5,6,7,8};
        int key = 4;
        int result = linearSearch(data, key);
        if (result !=-1){
            System.out.println("Element found at : " +result);
        }
        else{
            System.out.println("No element found");
        }
    }
}
