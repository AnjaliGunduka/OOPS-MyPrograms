package LinkedHashSet;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashSetExample {
	private static final int MAX = 6;
	public static void main(String[] args) {
		LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
		new LinkedHashMap<Integer, String>() {
			protected boolean removeEldestEntry(Map.Entry<Integer, String> eldest) {
				return size() > MAX;
			}
		};

		// Add mappings using put method
		linkedHashMap.put("Anjali", "Anju");
		linkedHashMap.put("Anusha", "Anu");
		linkedHashMap.put("SaiTeja", "Sai");
		System.out.println(linkedHashMap);
		System.out.println("Size of the map: " + linkedHashMap.size());

		System.out.println("Is map empty? " + linkedHashMap.isEmpty());

		System.out.println("Contains key 'two'? " + linkedHashMap.containsKey("two"));

		System.out.println("Contains value 'deepak" + "Deepak'? " + linkedHashMap.containsValue("deepak" + "Deepak"));

	}
}
