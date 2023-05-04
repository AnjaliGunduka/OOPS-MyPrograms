package AbstarctSetExamples;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

public class RemoveAllExample {
	public static void main(String[] args) {
		AbstractSet<Integer> Example1 = new TreeSet<Integer>();

		// Populating abs_set
		Example1.add(1);
		Example1.add(2);
		Example1.add(3);
		Example1.add(4);
		Example1.add(5);

		// print Example1
		System.out.println("AbstractSet before " + "removeAll() operation : " + Example1);

		// Creating another object of ArrayList<Integer>
		Collection<Integer> Example2 = new ArrayList<Integer>();
		Example2.add(1);
		Example2.add(2);
		Example2.add(3);

		// print Example2
		System.out.println("Collection Elements" + " to be removed : " + Example2);

		// Removing elements from AbstractSet
		// specified in Example2
		// using removeAll() method
		Example1.removeAll(Example2);

		// print arrlist1
		System.out.println("AbstractSet after " + "removeAll() operation : " + Example1);
	}
}
