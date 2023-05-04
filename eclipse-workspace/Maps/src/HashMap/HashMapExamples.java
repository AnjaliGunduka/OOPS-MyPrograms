package HashMap;

import java.util.HashMap;
import java.util.Map;

public class HashMapExamples {
	public static void main(String[] args) {
		Map<String, Integer> map = new HashMap<>();

		map.put("Ranjitha", 10);
		map.put("A", 30);
		map.put("B", 20);
		System.out.println("Size of map is:- " + map.size());
		Integer a = map.get("Ranjitha");// to get the values of strings
		System.out.println("value of a key is:-" + a);
		map.put("C", 2);// change 2 place to ravi next elemnt to 3
		map.remove("Ranjitha");
		map.put("D", 10);
		map.compute("E", (key, val) -> (val == null) ? 1 : val + 1);// update key value
		map.compute("E", (key, value) -> value + 3);// update the value


		for (Map.Entry<String, Integer> e : map.entrySet())
			System.out.println(e.getKey() + " " + e.getValue());
	}
}
