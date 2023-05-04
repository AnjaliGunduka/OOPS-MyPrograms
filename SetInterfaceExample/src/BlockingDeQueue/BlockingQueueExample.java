package BlockingDeQueue;

import java.util.Iterator;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class BlockingQueueExample {
	public static void main(String[] args) {
		BlockingDeque<Integer> example = new LinkedBlockingDeque<Integer>();

		// Add elements using add()
		example.add(134);
		example.add(245);
		example.add(23);
		example.add(122);
		example.add(90);
		// System.out.println(example);
		// Create an iterator to traverse the deque
		Iterator<Integer> Iter = example.iterator();
		for (int i = 0; i < example.size(); i++) {
			System.out.print(Iter.next() + " ");
		}
		if (example.remove(3)) {
			System.out.println("\n\nThe element 23 has been removed");
		} else {
			System.out.println("\n\nNo such element was found");
		}
		System.out.println(example);
	}

}
