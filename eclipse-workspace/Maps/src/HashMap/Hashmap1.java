package HashMap;

import java.util.HashMap;

public class Hashmap1 {
	public static void main(String[] args) { 
        HashMap<Integer, String > numbers = new HashMap<>(); 
 
        System.out.println("Initial HashMap: " + numbers); 
        // put() method to add elements 
        numbers.put(1, "One"); 
        numbers.put(2, "Two"); 
        numbers.put(3, "Three"); 
        System.out.println("HashMap after put(): " + numbers); 
 
        // get() method to get value 
        String value = numbers.get(2); 
        System.out.println("Value at index 1: " + value); 
        // using keySet() 
        System.out.println("Keys: " + numbers.keySet()); 
 
        // using values() 
        System.out.println("Values: " + numbers.values()); 
 
        // using entrySet() 
        System.out.println("Key/Value mappings: " + numbers.entrySet()); 
 
        // remove element associated with key 2 
        String value1 = numbers.remove(2); 
        System.out.println("Removed value: " + value1); 
 
        System.out.println("Updated HashMap: " + numbers); 
    } 

}
