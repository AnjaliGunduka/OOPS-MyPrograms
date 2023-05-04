package BlockingDeQueue;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class BlockedQueueMethods {
	public static void main(String[] args) {
		BlockingDeque<Integer> examples = new LinkedBlockingDeque<Integer>();

		// Add elements using add()
		examples.add(22);
		examples.add(125);
		examples.add(723);
		examples.add(172);
		examples.add(100);

		// Print the elements of lbdq on the console
		System.out.println("The LinkedBlockingDeque, example contains:");
		System.out.println(examples);

		if (examples.contains(22))
			System.out.println("The LinkedBlockingDeque, example contains 22");
		else
			System.out.println("No such element exists");
		int head = examples.element();
		System.out.println("The head of lbdq: " + head);

		// Using peekLast() to retrieve the tail of the deque
		int tail = examples.peekLast();
		System.out.println("The tail of lbdq: " + tail);
	}
}
