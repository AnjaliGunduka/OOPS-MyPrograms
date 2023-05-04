package Stack;

import java.util.Stack;

public class PushExample {
	public static void main(String[] args) {
		Stack stack1 = new Stack();

		// Initialization of Stack
		// using Generics
		Stack<String> stack2 = new Stack<String>();

		// pushing the elements
		stack1.push(4);
		stack1.push("All");
		stack1.push("Geeks");

		stack2.push("Geeks");
		stack2.push("For");
		stack2.push("Geeks");

		// Printing the Stack Elements
		System.out.println(stack1);
		System.out.println(stack2);
	}
}
