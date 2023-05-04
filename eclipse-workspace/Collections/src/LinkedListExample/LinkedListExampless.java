package LinkedListExample;

import java.util.LinkedList;

public class LinkedListExampless {
	public static void main(String[] args) {
		LinkedList<String> cars = new LinkedList<String>();
		cars.add("Volvo");
		cars.add("BMW");
		
		// Use addFirst() to add the item to the beginning
		cars.addFirst("Mazda");
		cars.addLast("Mazda");
		cars.removeFirst();
		cars.removeLast();
		System.out.println(cars);
		System.out.println(cars.getLast());
	
	}
}
