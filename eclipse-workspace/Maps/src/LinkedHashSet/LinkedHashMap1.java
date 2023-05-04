package LinkedHashSet;

import java.util.LinkedHashMap;

public class LinkedHashMap1 {
	 public static void main(String[] args) { 
	        LinkedHashMap<String, Integer> evenNumbers = new                                               LinkedHashMap<>(); 
	 
	        // Using put() 
	        evenNumbers.put("Two", 2); 
	        evenNumbers.put("Four", 4); 
	    
	        System.out.println("Original LinkedHashMap: " +evenNumbers); 
	        // Using putIfAbsent() 
	        evenNumbers.putIfAbsent("Six", 6); 
	        System.out.println("Updated LinkedHashMap(): " +evenNumbers); 
	 
	        //Creating LinkedHashMap of numbers 
	        LinkedHashMap<String, Integer> numbers = new                                                    LinkedHashMap<>(); 
	        numbers.put("One", 1); 
	 
	        // Using putAll() 
	        numbers.putAll(evenNumbers); 
	        System.out.println("New LinkedHashMap: " + numbers); 
	 
	        // Using entrySet() 
	        System.out.println("Key/Value mappings: " + numbers.entrySet()); 
	 
	        // Using keySet() 
	        System.out.println("Keys: " + numbers.keySet()); 
	 
	        // Using values() 
	        System.out.println("Values: " + numbers.values()); 
	 
	        // Using getOrDefault() 
	        int value2 = numbers.getOrDefault("Five", 5); 
	        System.out.println("Returned Number: " + value2); 
	 
	        // remove method with single parameter 
	        int value = numbers.remove("Two"); 
	        System.out.println("Removed value: " + value); 
	 
	        // remove method with two parameters 
	        boolean result = numbers.remove("Three", 3); 
	        System.out.println("Is the entry Three removed? " + result); 
	 
	        System.out.println("Updated LinkedHashMap: " + numbers); 
	    } 

}
