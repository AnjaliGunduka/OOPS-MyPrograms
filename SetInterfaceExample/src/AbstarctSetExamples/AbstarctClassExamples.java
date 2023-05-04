package AbstarctSetExamples;

import java.util.AbstractSet;
import java.util.TreeSet;

public class AbstarctClassExamples {
	public static void main(String[] args) {
		// Creating object of AbstractSet<Integer>
		AbstractSet<Integer> example = new TreeSet<Integer>();

		// Populating abs_set
		example.add(1);
		example.add(2);
		example.add(3);
		example.add(4);
		example.add(5);

		// print example
		System.out.println("AbstractSet: " + example);
	}
}
