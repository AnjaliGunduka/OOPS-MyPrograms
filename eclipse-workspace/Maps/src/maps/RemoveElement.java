package maps;

import java.util.HashMap;
import java.util.Map;

public class RemoveElement {
	public static void main(String[] args) {
		// Initialization of a Map
		// using Generics
		Map<Integer, String> hm1 = new HashMap<Integer, String>();

		// Inserting the Elements
		hm1.put(new Integer(1), "Geeks");
		hm1.put(new Integer(2), "For");
		hm1.put(new Integer(3), "Geeks");
		hm1.put(new Integer(4), "For");

		// Initial Map
		System.out.println(hm1);

		hm1.remove(new Integer(4));

		// Final Map
		System.out.println(hm1);
	}
}
