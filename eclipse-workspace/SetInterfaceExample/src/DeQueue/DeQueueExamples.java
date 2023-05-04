package DeQueue;

import java.util.ArrayDeque;
import java.util.Deque;
//import java.util.LinkedList;

public class DeQueueExamples {
	public static void main(String[] args) {
		Deque<String> deque = new ArrayDeque<String>();
		deque.add("A");
		deque.add("B");
		deque.add("C");
		System.out.println(deque);
		deque.addFirst("D");
		deque.addLast("E");
		deque.push("F");//Top of the list
		deque.offer("G");//last of the list
		deque.offerFirst("H");//first element
		System.out.println(deque);
		deque.pollFirst();//first element is removed
		System.out.println(deque);
		deque.peekFirst();
		System.out.println(deque);
	}
}
