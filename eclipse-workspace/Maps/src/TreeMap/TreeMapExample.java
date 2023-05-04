package TreeMap;

import java.util.TreeMap;

public class TreeMapExample {

	public static void main(String[] args) {
		TreeMap tm1 = new TreeMap();

		// Initialization of a TreeMap
		// using Generics
		TreeMap<Integer, String> tm2 = new TreeMap<Integer, String>();

		// Inserting the Elements
		tm1.put(3, "Geeks");
		tm1.put(2, "For");
		tm1.put(1, "Geeks");

		tm2.put(new Integer(3), "Geeks");
		tm2.put(new Integer(2), "For");
		tm2.put(new Integer(1), "Geeks");
		tm2.put(1, "Geeeks");

		System.out.println(tm1);
		System.out.println(tm2);
	}
}
