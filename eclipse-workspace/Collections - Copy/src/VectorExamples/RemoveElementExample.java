package VectorExamples;

import java.util.Vector;

public class RemoveElementExample {
	public static void main(String[] args) {
		Vector<Integer> vec = new Vector<>();
		// Add elements in the vector
		vec.add(1);
		vec.add(2);
		vec.add(3);
		vec.add(4);
		vec.add(5);
		vec.add(6);
		System.out.println("Vector element before removal: " + vec);
		// Remove an element
		boolean b = vec.removeElement(5);
		System.out.println("element removed succcessfully:" + b);
		// Checking vector and displays the element
		System.out.println("Vector element after removal: " + vec);
		//vec.isEmpty();
		//System.out.println("vector contains"+vec);
	}
}
