package PriorityQueue;

import java.util.Comparator;
import java.util.PriorityQueue;

public class ComparatorExamples {
	public static void main(String[] args) {
		// Creating an empty Priority_Queue
		PriorityQueue<Integer>  queue= new PriorityQueue<Integer>();

		// Adding elements to the queue
		queue.add(20);
		queue.add(24);
		queue.add(30); 
		queue.add(3);
		queue.add(45);
		queue.add(50);

		System.out.println("Priority queue values are: " + queue);

		// Creating a comparator
		Comparator comp = queue.comparator();
		// Displaying the comparator values
		System.out.println("Since the Comparator value is: " + comp);
		System.out.println("it follows natural ordering");
		 queue.offer(1); 
	        System.out.println("Updated PriorityQueue: " +  queue); 
	 
	        // Using the peek() method 
	        int number =  queue.peek(); 
	        System.out.println("Accessed Element: " + number); 
	    } 
	 

	 
	}

