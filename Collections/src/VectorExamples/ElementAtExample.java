package VectorExamples;

import java.util.Vector;

public class ElementAtExample {
	public static void main(String[] args) {
		// Create an empty vector
		Vector<String> colors = new Vector<String>();
		// Add elements in the vector
		colors.add("White");
		colors.add("Green");
		colors.add("Black");
		colors.add("Pink");
		// Get an element at the specified index
		System.out.println("Element at 0th position = " + colors.elementAt(0));
		System.out.println("Element at 2nd position = " + colors.elementAt(2));
	}
}
