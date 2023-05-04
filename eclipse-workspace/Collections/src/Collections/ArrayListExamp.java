package Collections;

import java.util.ArrayList;

public class ArrayListExamp {
	public static void main(String[] args) {
		ArrayList<String> bags = new ArrayList<String>();

		// Add values in the bags list.
		bags.add("pen");
		bags.add("pencil");
		bags.add("paper");

		// Creating another array list
		ArrayList<String> boxes = new ArrayList<String>();

		// Add values in the boxes list.
		boxes.add("pen");
		boxes.add("paper");
		boxes.add("books");
		boxes.add("rubber");

		// Before Applying method print both lists
		System.out.println("Bags Contains :" + bags);
		System.out.println("Boxes contains" + boxes);
		boxes.retainAll(bags);
		System.out.println("\nAfter Applying retainAll()" + " method to Boxes\n");
		System.out.println("Bags Contains :" + bags);
		System.out.println("Boxes Contains :" + boxes);

	}
}
