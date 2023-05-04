package VectorExamples;

import java.util.Vector;

public class RemoveEleIndexNumberExample {
public static void main(String[] args) {
	Vector<String> vector = new Vector<String>();
	 
    //Adding elements to the Vector
    vector.add("C++");
    vector.add("Java");
    vector.add("Cobol");
    vector.add("C");
    vector.add("Oracle");

    System.out.println("Vector elements before remove(): ");
    for(int i=0; i < vector.size(); i++)
    {
       System.out.println(vector.get(i));
    }
 
    // Removing 3rd element from Vector
    Object obj = vector.remove(2);
 
    System.out.println("\nElement removed from Vector is:");
    System.out.println(obj);
 System.out.println(vector);
}}
