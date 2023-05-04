package VectorExamples;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LListToAList {
	 public static void main(String[] args) { 
	     LinkedList<String> names = new LinkedList<String>(); 
	        names.add("Harry"); 
	        names.add("Jack"); 
	        names.add("Tim"); 
	        names.add("Rick"); 
	        names.add("Rock"); 
	     System.out.println("LinkedList elements :" + names); 
	 
	        //conversion 
	      List<String> names1 = new ArrayList<String>(names); 
	        System.out.println("ArrayList elements :"); 
	        for (String str : names1) { 
	            System.out.println(str); 
	        } 
	    } 

}
