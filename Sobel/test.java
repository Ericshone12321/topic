import java.util.ArrayList;

public class test {
    public static void main(String args[]) {
        final ArrayList <int[]> arr = new ArrayList<int[]>();
        int[] a = {1, 2};
        int[] b = {3, 4};
        arr.add(a);
        arr.add(b);
        arr.remove(0);

        System.out.println(arr.size());
        
    }
}
