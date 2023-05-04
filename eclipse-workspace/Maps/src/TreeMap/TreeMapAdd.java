package TreeMap;

import java.util.SortedMap;
import java.util.TreeMap;

public class TreeMapAdd {
	public static void main(String[] args) {
		TreeMap<Integer, String> tree_map = new TreeMap<Integer, String>();
		SortedMap<Integer, String> map_head = new TreeMap<Integer, String>();
// Mapping string values to int keys
		tree_map.put(10, "Anjali");
		tree_map.put(15, "Anu");
		tree_map.put(20, "Sai");
		tree_map.put(25, "Deepak");
		tree_map.put(30, "Gunduka");

// Displaying the TreeMap
		System.out.println("TreeMap: " + tree_map);
		System.out.println("The cloned map look like this: " + tree_map.clone());// copy elments
		System.out.println("The set is: " + tree_map.entrySet());// create a new set and store the map elements into
																	// them.
		System.out.println("Lowest entry is: " + tree_map.firstEntry());// lowest entry
		System.out.println("The Value is: " + tree_map.get(22));// print the 25 contains value or null
		// Creating the sorted map for map head
		map_head = tree_map.headMap(20);
		System.out.println(map_head);// we have create a tree_mapThe method returns a view of the portion of the map
										// strictly less than the parameter key_value.
		System.out.println("The set is: " + tree_map.keySet());// values are set
		System.out.println("The last key is " + tree_map.lastKey());
		// Inserting existing key along with new value
		String returned_value = (String) tree_map.put(20, "All");

		// Verifying the returned value
		System.out.println("Returned value is: " + returned_value);
		System.out.println("The subMap is " + tree_map.subMap(15, 30));// in between values are printed
	}

}
