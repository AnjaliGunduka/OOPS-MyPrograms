package PriorityQueue;

import java.util.PriorityQueue;
import java.util.Queue;

public class PriorityQueueExample {
	public static void main(String[] args) {
		Queue<Integer> pQueue = new PriorityQueue<Integer>();

		// Adding items to the pQueue using add()
		pQueue.add(10);
		pQueue.add(20);
		pQueue.add(15);

		// Printing the top element of PriorityQueue
		System.out.println("Access top most elemnt:" + pQueue.peek());

		// Printing the top element and removing it
		// from the PriorityQueue container
		System.out.println("Removin top of the element:" + pQueue.poll());

		// Printing the top element again
		System.out.println("top of the element:" + pQueue.peek());
		System.out.println(pQueue);
	}
}
