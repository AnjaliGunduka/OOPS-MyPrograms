package SortedSet;

import java.util.SortedSet;
import java.util.TreeSet;

public class SortedSetDemo {
	public static void main(String[] args) { 
        SortedSet<Integer> numbers = new TreeSet<>(); 
 
        // Insert elements to the set 
        numbers.add(1); 
        numbers.add(6); 
        numbers.add(9); 
        numbers.add(4); 
        System.out.println("SortedSet: " + numbers); 
 
        // Access the element 
        int firstNumber = numbers.first(); 
      System.out.println("First Number: " + firstNumber); 
 
        int lastNumber = numbers.last(); 
        System.out.println("Last Number: " + lastNumber); 
 
        // Remove elements 
        boolean result = numbers.remove(2); 
        System.out.println("Is the number 2 removed? " + result); 
     

//subset elements 
System.out.println("subSet : "+numbers.subSet(2, 4)); 
 
//tail element 
System.out.println("Tail element : "+numbers.tailSet(3)); 

} 

}
