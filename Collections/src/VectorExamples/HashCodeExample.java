package VectorExamples;

import java.util.Vector;

public class HashCodeExample {
public static void main(String[] args) {
	Vector<Integer> nums = new Vector<>();  
    //Add elements in the vector  
    nums.add(1);  
    nums.add(2);  
    nums.add(3);  
    nums.add(4);  
    nums.add(5);  
    nums.add(6);  
    nums.add(7);  
    nums.add(8);    
    System.out.println(nums.hashCode());
  
 
}
}
