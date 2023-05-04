package Stack;

import java.util.Stack;

public class PopExample {
	public static void main(String[] args) {
		Stack<String> STACK = new Stack<String>();

		// Use add() method to add elements
		STACK.push("Deepak");
		STACK.push("Sai");
		STACK.push("Anu");
		STACK.push("Anjali");
		STACK.push("bro&sis");

		// Displaying the Stack
		System.out.println("Initial Stack: " + STACK);

		// Removing elements using pop() method
		System.out.println("Popped element: " + STACK.pop());
		System.out.println("Popped element: " + STACK.pop());

		// Displaying the Stack after pop operation
		System.out.println("Stack after pop peration " + STACK);
	}
}
