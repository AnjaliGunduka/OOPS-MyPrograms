package TreeSet;

import java.util.Iterator;
import java.util.TreeSet;

public class TailSetExample {
	public static void main(String[] args) {
		TreeSet<Integer> tree_set = new TreeSet<Integer>();

		// Adding the elements using add()
		tree_set.add(1);
		tree_set.add(2);
		tree_set.add(3);
		tree_set.add(4);
		tree_set.add(5);
		tree_set.add(10);
		tree_set.add(20);
		tree_set.add(30);
		tree_set.add(40);
		tree_set.add(50);

		// Creating the tailSet tree
		TreeSet<Integer> tail_set = new TreeSet<Integer>();

		// Limiting the values till 5
		tail_set = (TreeSet<Integer>) tree_set.tailSet(10);

		// Creating an Iterator
		Iterator iterate;
		iterate = tail_set.iterator();

		// Displaying the tree set data
		System.out.println("The resultant values from the tail: ");

		// Iterating through the tailSet
		while (iterate.hasNext()) {
			System.out.println(iterate.next() + " ");
		}
	}
}
