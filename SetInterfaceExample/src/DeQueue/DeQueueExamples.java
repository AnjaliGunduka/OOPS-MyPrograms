package DeQueue;

import java.util.ArrayDeque;
import java.util.Deque;
//import java.util.LinkedList;

public class DeQueueExamples {
	public static void main(String[] args) {
		Deque<String> deque = new ArrayDeque<String>();
		deque.add("anju");
		deque.add("anu");
		deque.add("anjali");
		System.out.println(deque);
		deque.addFirst("anjali");
		deque.addLast("gunduka");
		deque.push("anjaliGunduka");//Top of the list
		deque.offer("GundukaAnjali");//last of the list
		deque.offerFirst("AnjuBaby");//first element
		System.out.println(deque);
		deque.pollFirst();//first element is removed
		System.out.println(deque);
		deque.peekFirst();
		System.out.println(deque);
	}
}
