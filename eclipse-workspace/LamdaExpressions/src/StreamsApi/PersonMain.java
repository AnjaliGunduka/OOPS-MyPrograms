package StreamsApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersonMain {
	public static void main(String[] args) {
		// List of person
		List<Person> personList = new ArrayList<>();
		// Add Elements
		personList.add(new Person("vicky", 24));
		personList.add(new Person("poonam", 25));
		personList.add(new Person("sachin", 19));
 
		// A comparator class with multiple
		// comaparator methods
		ComparisonProvider comparator = new ComparisonProvider();

		// Use instance method reference
		// to sort array by name
		Collections.sort(personList, comparator::compareByName);
		System.out.println("Sort by name :");
		personList.stream().map(x -> x.getName()).forEach(System.out::println);

		// Use instance method reference
		// to sort array by age
		Collections.sort(personList, comparator::compareByAge);
		System.out.println("Sort by age :");
		personList.stream().map(x -> x.getName()).forEach(System.out::println);
	}
}
