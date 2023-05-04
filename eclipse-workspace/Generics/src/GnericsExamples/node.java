package GnericsExamples;

public class node<T> {
	// 1. Storing value of node
	T names;

	public node(T names) {
		super();
		this.names = names;
	}

// 2. Storing address of next node
	node<T> next;
}

class list<T> {
	node<T> studentMarks;
	private int length = 0;

	public list() {
		super();
	}

	void add(T names) {
		node<T> temp = new node<>(names);

		if (this.studentMarks == null)

		{
			studentMarks = temp;
		}

// If list already exists
		else {

			// Temporary node for traversal
			node<T> X = studentMarks;

			// Iterating till end of the List
			while (X.next != null) {
				X = X.next;
			}

			// Adding new valued node at the end of the list
			X.next = temp;
		}

// Increasing length after adding new node
		length++;
	}

	public static void main(String[] args) {
		list<Integer> list1 = new list<>();
		System.out.println("Integer LinkedList created as list1 :");
		// Adding elements to the above List object

		// Element 1 - 100
		list1.add(100);
		// Element 2 - 200
		list1.add(200);
		// Element 3 - 300
		list1.add(300);

		// Display message only
		System.out.println("list1 after adding 100,200 and 300 :");

		// Print and display the above List elements
		System.out.println(list1);

	}

}