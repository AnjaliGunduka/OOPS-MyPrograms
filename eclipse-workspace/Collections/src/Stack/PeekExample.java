package Stack;

import java.util.Stack;

public class PeekExample {
public static void main(String[] args) {
	 // Creating an empty Stack
    Stack<String> STACK = new Stack<String>();

    // Use push() to add elements into the Stack
    STACK.push("Deepak");
	STACK.push("Sai");
	STACK.push("Anu");
	STACK.push("Anjali");
	STACK.push("bro&sis");


    // Displaying the Stack
    System.out.println("Initial Stack: " + STACK);

    // Fetching the element at the head of the Stack
    System.out.println("The element at the top of the"
                       + " stack is: " + STACK.peek());

    // Displaying the Stack after the Operation
    System.out.println("Final Stack: " + STACK);
}
}
