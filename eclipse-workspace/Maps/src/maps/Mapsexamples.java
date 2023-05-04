package maps;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Mapsexamples {
	public static void main(String[] args) {
		Map<String, Integer> map = new HashMap<>();
		Set<String> s = new HashSet<>();
		map.put("vishal", 10);
		map.put("sachin", 30);
		map.put("vaibhav", 20);
		map.put("Geeks", new Integer(1));// Adding an element
		// map.put("Geeks", new Integer(1));// changing ana element in place 1
		map.remove(new String("vishal"));// remove an element
		System.out.println("Is the key '20' present? " + map.containsKey("sachin"));// contains elemnt or not
		System.out.println("The set is: " + map.entrySet());// display the set
		System.out.println("the set is:"+map.keySet());//dispaly in values first given in put
		System.out.println(map);
		for (Map.Entry<String, Integer> e : map.entrySet())

			System.out.println(e.getKey() + " " + e.getValue());
	}
}
