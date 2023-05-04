package ArrayDeque;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class ArrayDeQueueExample {
	public static void main(String[] args) {
		Deque<String> deque = new ArrayDeque<String>();
		deque.add("anju");
		deque.add("anu");
		deque.add("anjali");
		 deque.push("265");
		for (String str : deque) {
			System.out.println("names are:-" + str);
		}
		deque.pop();// top elemnt is removed
		System.out.println("pop afrer");

		for (Iterator itr = deque.iterator(); itr.hasNext();) {
			System.out.println(itr.next());
		}
	
		 for (Iterator dItr = deque.descendingIterator(); dItr.hasNext();) {
		 System.out.println(dItr.next());
		 }
		System.out.println("Head elements of:-" + deque.element());//head elemnts
		 Object[] arr = deque.toArray();
	        System.out.println("\nArray Size : " + arr.length);
	        
	        System.out.print("Array elements : ");
	        for(int i=0; i<arr.length ; i++)
	            System.out.print(" " + arr[i]);
	        System.out.println(deque);
	}
}
