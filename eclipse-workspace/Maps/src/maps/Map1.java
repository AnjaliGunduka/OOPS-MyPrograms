package maps;

import java.util.HashMap;
import java.util.Map;

public class Map1 {
	public static void main(String[] args) { 
        Map<String, Integer> numbers = new HashMap<>(); 
 
        // Insert elements to the map 
        numbers.put("One", 1); 
        numbers.put("Two", 2); 
        System.out.println("Map: " + numbers); 
 
        // Access keys of the map 
        System.out.println("Keys: " + numbers.keySet()); 
 
        // Access values of the map 
        System.out.println("Values: " + numbers.values()); 
 
        // Access entries of the map 
        System.out.println("Entries: " + numbers.entrySet()); 
 
        // Remove Elements from the map 
        int value = numbers.remove("Two"); 
        System.out.println("Removed Value: " + value); 
    } 

}
