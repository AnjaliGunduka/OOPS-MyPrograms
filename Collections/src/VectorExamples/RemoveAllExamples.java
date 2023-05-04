package VectorExamples;

import java.util.Vector;

public class RemoveAllExamples {
public static void main(String[] args) {
	Vector<String> vec1 = new Vector<String>();  
    //Add elements in the Vector1  
    vec1.add("anjali");  
    vec1.add("anu");  
    vec1.add("anju");  
    vec1.add("10");  
    vec1.add("20");   
    //Displaying the Vector1 elements  
    System.out.println("Vector: " + vec1);   
    //Create an empty Vec2  
    Vector<String> vec2 = new Vector<String>();   
    //Add elements in the Vector2  
    vec2.add("anjali");  
    vec2.add("anu");  
    vec2.add("C");   
    //use removeAll() method  
    boolean changed = vec1.removeAll(vec2);  
    //Print the result  
    if (changed)  
        System.out.println("Collection removed");  
    else  
        System.out.println("Collection not removed");  
    //Display the final Vector  
    System.out.println("Final Vector: " + vec1);   
}
}
