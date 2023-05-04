package BlockingQueue;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DrainToExample {

	public class Employee {

		public String name;
		public String position;
		public String salary;

		Employee(String name, String position, String salary) {
			this.name = name;
			this.position = position;
			this.salary = salary;
		}

		@Override
		public String toString() {
			return "Employee [name=" + name + ", position=" + position + ", salary=" + salary + "]";
		}
	}

	// Main Method
	public static void main(String[] args) {
		DrainToExample gfg = new DrainToExample();
		gfg.containsMethodExample();
	}

	public void containsMethodExample() {

		// define capacity of BlockingQueue
		int capacity = 50;

		// create object of BlockingQueue
		BlockingQueue<Employee> BQ = new LinkedBlockingQueue<Employee>(capacity);

		// create a ArrayList to pass as parameter to drainTo()
		ArrayList<Employee> collection = new ArrayList<Employee>();

		// add Employee object to queue
		Employee emp1 = new Employee("Aman", "Analyst", "24000");
		Employee emp2 = new Employee("Sachin", "Developer", "39000");
		BQ.add(emp1);
		BQ.add(emp2);

		// printing Arraylist and queue
		System.out.println("Before drainTo():");
		System.out.println("BlockingQueue : \n" + BQ.toString());
		System.out.println("ArrayList : \n" + collection);

		// Apply drainTo method and pass collection as parameter
		int response = BQ.drainTo(collection);

		// print no of element passed
		System.out.println("\nNo of element passed: " + response);

		// printing Arraylist and queue after applying drainTo() method
		System.out.println("\nAfter drainTo():");
		System.out.println("BlockingQueue : \n" + BQ.toString());
		System.out.println("ArrayList : \n" + collection);
	}

}
