package VectorExamples;

import java.util.Vector;

public class SetElementAtExample {
public static void main(String[] args) {
	 Vector<Integer> vec = new Vector<>();  
     //Add elements in the vector  
     vec.add(1);  
     vec.add(2);  
     vec.add(3);  
     vec.add(4);  
     vec.add(6);  
     //Displaying the vector element  
     System.out.println("Vector element before setElementAt(): " +vec);  
     //Set Element 5 at 4th index position  
     vec.setElementAt(5, 4);           
     //Displaying the vector element again  
     System.out.println("Vector element after setElementAt(): "+vec);  
}
}
