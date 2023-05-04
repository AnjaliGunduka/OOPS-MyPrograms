package VectorExamples;

import java.util.Vector;

public class GetExample {
public static void main(String[] args) {
	 Vector<Integer> vec = new Vector<>();  
     //Add element in the vector  
     vec.add(1);  
     vec.add(2);  
     vec.add(3);       
     vec.add(4);  
     vec.add(5);  
     //Get the element at specified index  
     System.out.println("Element at index 1 is = "+vec.get(1));  
     System.out.println("Element at index 3 is = "+vec.get(3));    
}
}
