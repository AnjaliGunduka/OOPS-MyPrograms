package BlockingQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueExample {
public static void main(String[] args) throws InterruptedException {
	// define capacity of ArrayBlockingQueue
    int capacity = 5;
    // create object of ArrayBlockingQueue
    BlockingQueue<String> queue = new ArrayBlockingQueue<String>(capacity);
    // Add elements to ArrayBlockingQueue using put
    // method
    queue.put("Gunduka");
    queue.put("Anjali");
    queue.put("Anu");
    queue.put("Anju");
    queue.put("Anusha");
    // print Queue
    System.out.println("queue contains " + queue);
    // remove some elements
    queue.remove();
    queue.remove();
    // Add elements to ArrayBlockingQueue
    // using put method
    queue.put("JavaDeveloper");
    queue.put("Civil");
   // queue.put("Civils");
    System.out.println("queue contains:- " + queue);
}
}
