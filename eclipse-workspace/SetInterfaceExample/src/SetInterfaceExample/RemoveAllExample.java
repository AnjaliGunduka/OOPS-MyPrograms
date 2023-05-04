package SetInterfaceExample;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RemoveAllExample {
public static void main(String[] args) {
	Set<Integer> a = new HashSet<Integer>();
	a.addAll(Arrays.asList(new Integer[] { 1, 3, 2, 4, 8, 9, 0 }));
	Set<Integer> b = new HashSet<Integer>();
	b.addAll(Arrays.asList(new Integer[] { 1, 3, 7, 5, 4, 0, 7, 5 }));
	Set<Integer> difference = new HashSet<Integer>(a);
	difference.removeAll(b);
	System.out.print("Difference of the two Set");
	System.out.println(difference);

}
}
