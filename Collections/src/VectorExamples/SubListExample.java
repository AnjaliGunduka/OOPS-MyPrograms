package VectorExamples;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class SubListExample {
public static void main(String[] args) {
	Vector<Integer> vec = new Vector<>();  
    //Add elements in the vector  
    vec.add(1);  
    vec.add(2);  
    vec.add(3);  
    vec.add(4);  
    vec.add(5);  
    vec.add(6);  
    vec.add(7);  
    vec.add(8);       
    //Display the vector elements   
    System.out.println("The vector elements are: "+vec);  
    //Use subList() method to get subList of the vector  
      List<Integer> subList = vec.subList(3,6);    
      //Display sublist vector elements  
      System.out.println("Sublist elements: ");  
      for(int i=0; i < subList.size() ; i++){  
         System.out.println(subList.get(i));  
         System.out.println("min value:"+Collections.min(vec));
                    
   }  
}
}
