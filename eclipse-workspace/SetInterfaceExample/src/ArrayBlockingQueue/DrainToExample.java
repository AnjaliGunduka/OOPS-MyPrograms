package ArrayBlockingQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class DrainToExample {
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		int capacity = 100;
		ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(capacity);
		queue.add("Anjali");
		queue.add("Anu");
		queue.add("sai");
		queue.add("deepu");
		System.out.println("Elements int the queue = " + queue);
		/*
		 * drainTo() method removes at most the given number of available elements from
		 * this queue and adds them to the list.
		 */
		queue.drainTo(list,2);
		System.out.println("Elements left in the queue : " + queue);
		System.out.println("Elements drained in the list : " + list);
	}
}