package GnericsExamples;

import java.util.HashMap;
import java.util.Map;

public class UsingMaps {
	public static void main(String[] args) {

		// create array of strings
		String arr[] = { "Anjali", "Anusha", "Anjali", "Anu" };

		// to count the frequency of a string and store it
		// in the map
		Map<String, Integer> map = new HashMap<>();
		for (int i = 0; i < arr.length; i++) {
			if (map.containsKey(arr[i])) {
				int count = map.get(arr[i]);
				map.put(arr[i], count + 1);
			} else {
				map.put(arr[i], 1);
			}
		}

		// to get and print the count of the mentioned
		// string
		System.out.println(map.get("Anjali"));
		System.out.println(map.get("Anusha"));
	}
}
