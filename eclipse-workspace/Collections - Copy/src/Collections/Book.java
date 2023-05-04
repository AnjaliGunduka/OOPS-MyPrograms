package Collections;

import java.util.ArrayList;
import java.util.List;

public class Book {
	int id;
	String name;
	int quantity;

	Book(int id, String name, int quantity) {
		this.id = id;
		this.name = name;
//		this.author = author;
//		this.publisher = publisher;
		this.quantity = quantity;
	}
}

class ArrayListExample20 {
	public static void main(String[] args) {
		// Creating list of Books
		List<Book> list = new ArrayList<Book>();
		// Creating Books
		Book b1 = new Book(101, "Maths", 8);
		Book b2 = new Book(102, "Dbms", 4);
		Book b3 = new Book(103, "java", 6);
		// Adding Books to list
		list.add(b1);
		list.add(b2);
		list.add(b3);
		// Traversing list
		for (Book b : list) {
			System.out.println(b.id + " " + b.name + " " + b.quantity);
			System.out.println(b);
		}
	}
}
