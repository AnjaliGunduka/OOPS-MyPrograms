package VectorExamples;

import java.util.Collections;
import java.util.Vector;

public class ReplaceAllExample {
	public static void main(String[] args) {
		Vector<Integer> vec = new Vector<>();
		// Add elements in the Vector
		vec.add(10);
		vec.add(2);
		vec.add(30);
		vec.add(40);
		vec.add(2);
		// Display vector elements
		System.out.println("Vector elements: " + vec);
//replace all vector element "2" with "20"  
		Collections.replaceAll(vec, 3, 20);
//Display vector elements after replacement  
		System.out.println("New vector elements: " + vec);
	}
}
