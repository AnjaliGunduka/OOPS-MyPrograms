package AbstractCollection;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;

public class AbstarctCollection {
	public static void main(String[] args) {
		AbstractCollection<Object> abs = new ArrayList<Object>();

// Use add() method to add
// elements in the collection
		abs.add("Welcome");
		abs.add("To");
		abs.add("Geeks");
		abs.add("4");
		
		AbstractList<String> places1 = new ArrayList<>(); 
        places1.add("Hi-Tech city"); 
        places1.add("Kukatpally"); 
        places1.add("Jubliee Hills"); 
        places1.add("Banjara Hills"); 
        System.out.println("Second list : " + abs); 
 
        //comparing two lists 
        boolean ab = abs.equals(places1); 
       System.out.println("Two lists are equal : " + ab); 
 

// Displaying the Collection
		System.out.println("AbstractCollection: " + abs);
		System.out.println();
		int lastindex = ((ArrayList<Object>) abs).lastIndexOf("A"); 
        System.out.println("Last index " + lastindex); 
    } 

	}

