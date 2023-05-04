package LamdaExpressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

import StreamsApi.College;
import StreamsApi.Employee;
import StreamsApi.Student;

public class CollegeFunction {
	public static void main(String[] args) {
		College c1 = new College(534, "Anjali", 800);
		College c2 = new College(531, "Neha", 600);
		College c3 = new College(513, "Preethi", 700);
		College c4 = new College(510, "Lalli", 500);
		College c5 = new College(550, "Nirosha", 900);
		List<College> list = new ArrayList<College>();
		list.add(c1);
		list.add(c2);
		list.add(c3);
		list.add(c4);
		list.add(c5);
		System.out.println(list);
		/**
		 * 1.marks greather than 800
		 */
		list.stream().filter(e -> e.getMarks() > 800).map(College::getId).collect(Collectors.toCollection(TreeSet::new))
				.forEach(System.out::println);
		/**
		 * 
		 */
		list.stream().filter(e -> e.getMarks()%2==0).map(College::getId).collect(Collectors.toCollection(TreeSet::new))
		.forEach(System.out::println);
		/**
		 * 2.First Student of the collge
		 */
		Optional<College> op = list.stream().findFirst();
		if (op.isPresent()) {
			System.out.println(op.get());
		} else {
			System.out.println("no value");
		}	
//		System.out.println(op.isPresent());//True or false it will print
		/**
		 * 3.UpperCase Letters
		 */
		list.stream().map(n->n.getName().toUpperCase()).forEach(n->System.out.println(n+""));
		
		
		
		
       
}
}
