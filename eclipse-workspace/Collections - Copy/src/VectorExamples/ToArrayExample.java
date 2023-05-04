package VectorExamples;

import java.util.Vector;

public class ToArrayExample {
	public static void main(String[] args) {
		Vector<Integer> vec = new Vector<Integer>(5);
		// Create an array with an initial capacity of 4
		Integer[] anArray = new Integer[5];
		// Add elements in the vector
		vec.add(1);
		vec.add(2);
		vec.add(3);
		vec.add(4);
		vec.add(5);
		// Fill the array from the vector
		vec.toArray(anArray);
		// Display the contents of an array
		System.out.println("Elements are: ");
		for (int i = 0; i < anArray.length; i++) {
			System.out.println(anArray[i]);
		}
	}
}
