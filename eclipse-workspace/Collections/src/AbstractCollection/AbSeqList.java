package AbstractCollection;

import java.util.AbstractSequentialList;
import java.util.LinkedList;

public class AbSeqList {
	public static void main(String[] args) { 
		AbstractSequentialList<String> ides = new LinkedList<>(); 
		        //adding elements 
		        ides.add("Ecllipse"); 
		        ides.add("Intellij"); 
		        ides.add("VS code"); 
		        System.out.println("Initial list :"+ides); 
		 
		        //removing element 
		        ides.remove(2); 
		 
		        //obtaining the specified element 
		        System.out.println("element is "+ides.get(1)); 
		 
		        //isEmpty method 
		   System.out.println("List is empty : "+ides.isEmpty()); 
		   System.out.println("Initial list :"+ides); 
		    } 

}
