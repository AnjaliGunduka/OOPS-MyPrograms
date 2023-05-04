package ArrayBlockingQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ArrayBlockingQueueExample {
	public static void main(String[] args) throws InterruptedException {
		int capacity = 10;
		BlockingQueue<String> queue = new ArrayBlockingQueue<String>(capacity);
		// add() method inserts the elements at the tail of this queue
		queue.add("Anjali");
		queue.add("Anu");
		queue.add("Sai");
		queue.add("Deepak");
		// offer() method inserts the elements at the tail of this queue if space is
		// available
		queue.offer("Gunduka");
		// put() method inserts the element at the tail of this queue , waiting for
		// space to available.
		queue.put("Geetanjali");
		// forEach() method performs the iterable for each element.
		System.out.println("Elements : ");
		for (String xyz : queue) {
			System.out.println(xyz);
		}
		/*
		 * contains() method will return a boolean value if the specified object is
		 * present in the queue
		 */
		System.out.println("queue.contains(Deepak) will return " + queue.contains("Deepak"));

	}
}
