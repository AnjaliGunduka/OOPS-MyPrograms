package SetInterfaceExample;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class addAllExample {
	public static void main(String[] args) {
		Set<Integer> a = new HashSet<Integer>();
		a.addAll(Arrays.asList(new Integer[] { 1, 3, 2, 4, 8, 9, 0 }));
		Set<Integer> b = new HashSet<Integer>();
		b.addAll(Arrays.asList(new Integer[] { 1, 3, 7, 5, 4, 0, 7, 5 }));

		// To find union
		Set<Integer> union = new HashSet<Integer>(a);
		union.addAll(b);
		System.out.print("Union of the two Set");
		System.out.println(union);
		
	}
}
