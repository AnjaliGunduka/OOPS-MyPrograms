package VectorExamples;

import java.util.Vector;

public class EnsureCapacityExamples {
	public static void main(String[] args) {
		Vector<String> vecobject = new Vector<>();
		// Add element in the vector
		vecobject.add("50");
		vecobject.add("green");
		vecobject.add("red");
		System.out.println("Element: " + vecobject);
		System.out.println("Capacity of the vector is: " + vecobject.capacity());
		// Increases the capacity of this vector
		vecobject.ensureCapacity(20);
		System.out.println("New Capacity of the vector is:" + vecobject.capacity());
	}
}
