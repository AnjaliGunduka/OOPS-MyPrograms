package TreeSet;

import java.util.TreeSet;

public class CeilingExample {
	public static void main(String[] args) {
		TreeSet<Integer> set = new TreeSet<Integer>();
		set.add(24);
		set.add(66);
		set.add(12);
		set.add(15);

		// Print the TreeSet
		System.out.println("TreeSet: " + set);

		// getting ceiling value for 25
		// using ceiling() method
		int value = set.ceiling(100);

		// printing the ceiling value
		System.out.println("Ceiling value for 100: " + value);
	}
}
