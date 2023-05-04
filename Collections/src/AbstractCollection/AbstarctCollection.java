package AbstractCollection;

import java.util.AbstractCollection;
import java.util.ArrayList;

public class AbstarctCollection {
	public static void main(String[] args) {
		AbstractCollection<Object> abs = new ArrayList<Object>();

// Use add() method to add
// elements in the collection
		abs.add("Welcome");
		abs.add("To");
		abs.add("Geeks");
		abs.add("4");
		abs.add("Geeks");

// Displaying the Collection
		System.out.println("AbstractCollection: " + abs);
		System.out.println();
	}
}
