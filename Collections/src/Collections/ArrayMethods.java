package Collections;

import java.util.ArrayList;
import java.util.ListIterator;

public class ArrayMethods {
	public static void main(String[] args) {
		ArrayList<Integer> arrlist = new ArrayList<Integer>(5);// create an empty array list1 with initial
        // capacity 5
		arrlist.add(15);// use add() method to add elements in the list
		arrlist.add(20);
		arrlist.add(25);
		arrlist.add(1, 35);// Here we adding an elements at the position 1
		System.out.println(arrlist);
		ArrayList<Integer> arrlist2 = new ArrayList<Integer>(5);
		 arrlist2.add(25);
	        arrlist2.add(30);
	        arrlist2.add(31);
	        arrlist2.add(35);
	        System.out.println(arrlist2);
	        
	       // inserting all elements of list2 at third 
	        // position
        arrlist.addAll(0, arrlist2);//If we want list2 elements to be inserted in the specified index postion
	        arrlist.addAll(arrlist2);//To print all the elements in one line
	        System.out.println(arrlist);
	        arrlist.ensureCapacity(400);
	        System.out.println("ArrayList can now"
                    + " surely store upto"
                    + " 400 elements.");
	        boolean ans = arrlist.contains(35);
	        ans = arrlist.contains(35);
	        
	        if (ans)
            System.out.println("The arrlist contains 35");
        else
            System.out.println("The arrlist does not contains 35");
	        int element = arrlist.get(2);
	        System.out.println(element);
	       // int pos =arrlist.indexOf(0);
	       // System.out.println(pos);
	        arrlist.add(1);
	        
	        // check if the list is empty or not
	        ans = arrlist.isEmpty();
	        if (ans == true)
	            System.out.println("The ArrayList is empty");
	        else
	            System.out.println("The ArrayList is not empty");
	    
	       // arrlist.removeAll(arrlist2);
	        //System.out.println(arrlist);
	     // apply removeIf() method
	        // we are going to remove numbers divisible by 3
	        //arrlist.removeIf(n -> (n % 3 == 0)); to remove an elements
	        //System.out.println(arrlist);
//	        arrlist.removeRange(0,2);
//	        System.out.println(arrlist);
	       // arrlist.retainAll(arrlist2);//to print the duplicate elements
	       // System.out.println(arrlist);
	     // Replacing element at the index 3 with 30
            // using method set()
           arrlist.set(1,100);
            System.out.println(arrlist);
            int j = arrlist.set(3, 30);
            System.out.println(j);//If we want to print which is removed then enter the variable name(i)
	}
}
