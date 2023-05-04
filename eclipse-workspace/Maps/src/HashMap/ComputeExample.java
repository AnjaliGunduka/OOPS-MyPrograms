package HashMap;

import java.util.HashMap;
import java.util.Map;

public class ComputeExample {
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<>();
		map.put("Name", "Aman");
		map.put("Address", "Kolkata");

		// Print the map
		System.out.println("Map: " + map);

		// remap the values using compute() method
		map.compute("Name", (key, val) -> val.concat(" Singh"));
		map.compute("Address", (key, val) -> val.concat(" West-Bengal"));

		// print new mapping
		System.out.println("New Map: " + map);
	}
}
