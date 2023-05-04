package VectorExamples;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class VecToList {
	public static void main(String[] args) { 
        Vector<String> names = new Vector<String>(); 
        names.add("Joe Root"); 
        names.add("Jos Buttler"); 
        names.add("Sam curran"); 
        names.add("Ben stokes"); 
 
        // Displaying Vector elements 
        System.out.println("Vector Elements :"); 
        for (String str : names){ 
            System.out.println(str); 
        } 
 
        //Converting Vector to List 
List<String> names1 = Collections.list(names.elements()); 
 
        //Displaying List Elements 
        System.out.println("List Elements :"); 
        for (String str2 : names1){ 
            System.out.println(str2); 
        } 
    } 

}
