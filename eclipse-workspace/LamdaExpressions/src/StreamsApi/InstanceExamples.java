package StreamsApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InstanceExamples {
	private String name;
	private Integer age;

	// Constructor
	public InstanceExamples(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public Integer getAge() {
		return age;
	}

	public String getName() {
		return name;
	}
}

class GFG {

	// A static method to
	// compare with name
	public static int compareByName(InstanceExamples a, InstanceExamples b) {
		return a.getName().compareTo(b.getName());
	}

	// A static method to
	// compare with age
	public static int compareByAge(InstanceExamples a, InstanceExamples b) {
		return a.getAge().compareTo(b.getAge());
	}

	// Main
	public static void main(String[] args) {
		// List of person
		List<InstanceExamples> personList = new ArrayList<>();
		// Add Elements
		personList.add(new InstanceExamples("vicky", 24));
		personList.add(new InstanceExamples("poonam", 25));
		personList.add(new InstanceExamples("sachin", 19));

		// Use static method reference to
		// sort array by name
		Collections.sort(personList, GFG::compareByAge);
		System.out.println("Sort by name :");
		personList.stream().map(x -> x.getName()).forEach(System.out::println);

		// Use static method reference
		// to sort array by age
		Collections.sort(personList, GFG::compareByName);
		System.out.println("Sort by age :");
		personList.stream().map(x -> x.getName()).forEach(System.out::println);
	}
}
