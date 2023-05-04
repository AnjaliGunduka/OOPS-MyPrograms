package VectorExamples;

import java.util.Vector;

public class LastElementExample {
public static void main(String[] args) {
	 Vector<String> vec = new Vector<>(4);   
     //Add elements in the vector          
     vec.add("Java");  
     vec.add("JavaScript");  
     vec.add("Android");  
     vec.add("Python");  
       //Obtain the last element of this vector  
   System.out.println("The last element of a vector is: " +vec.firstElement());  
}
}
