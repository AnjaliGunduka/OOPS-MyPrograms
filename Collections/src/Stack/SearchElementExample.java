package Stack;

import java.util.Stack;

public class SearchElementExample {
	public static void main(String[] args) {
		Stack<String> STACK = new Stack<String>();

		// Stacking strings
		STACK.push("Deepak");
		STACK.push("Sai");
		STACK.push("Anu");
		STACK.push("Anjali");
		STACK.push("bro&sis");

		// Displaying the Stack
		System.out.println("The stack is: " + STACK);

		// Checking for the element "4"
		System.out.println("Does the stack contains 'Sai'? " + STACK.search("Sai"));
		// Checking for the element "Hello"
		System.out.println("Does the stack contains 'Hello'? " + STACK.search("Hello"));

		// Checking for the element "Geeks"//
		System.out.println("Does the stack contains 'Deepak'? " + STACK.search("Deepak"));
	}
}
