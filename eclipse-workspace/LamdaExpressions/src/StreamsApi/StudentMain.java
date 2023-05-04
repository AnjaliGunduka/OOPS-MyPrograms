package StreamsApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentMain {
	public static void main(String[] args) {
		Student s = new Student(534, "Anjali", 30000);
		Student s1 = new Student(531, "Neha", 50000);
		Student s2 = new Student(513, "Lalitha", 60000);
		Student s3 = new Student(531, "Nirosha", 80000);
		Student s5 = new Student(510, "Preethi", 50000);
		List<Student> list = new ArrayList<Student>();
		list.add(s);
		list.add(s1);
		list.add(s2);
		list.add(s3);
		list.add(s5);
		
	
		/**
		 * 1.To check count of stsudents
		 */
		System.out.println("NO.of students are:-" + list.stream().count());
		/**
		 * 2.to check filtering data based on id(access name based on id)
		 */
		list.stream().filter(Student -> Student.id == 534)
				.forEach(Student -> System.out.println("name of student:-" + Student.name));
		
		list.stream().filter(Student -> Student.name == "Anjali")
		.forEach(Student -> System.out.println("name of student equalsIgnoreCase:-" + Student.name));
		/**
		 * 3.Who got maximum marks
		 * 
		 */
		Student stu = list.stream().max((marks1, marks2) -> marks1.marks > marks2.marks ? 1 : -1).get();
		System.out.println("Maximum marks of students:-" + stu.marks);
		/**
		 * 4.min marks of students
		 */
		Student stu1 = list.stream().min((marks1, marks2) -> marks1.marks > marks2.marks ? 1 : -1).get();
		System.out.println("Manimum marks of students:" + stu1.marks);
		/**
		 * 5.To get Object from list
		 */
		Optional<Student> a = list.stream().findAny();
		System.out.println(a.isPresent());
		/**
		 * 6.FindFirst Method
		 */
		Optional<Student> op = list.stream().findFirst();
		System.out.println(op.isPresent());
		/**
		 * 7.Acculate the names into treesetr
		 */
		Set<String> myNames = list.stream().map(Student::getName).collect(Collectors.toCollection(TreeSet::new));
		System.out.println("names are" + myNames);
		/**
		 * 8.group all the name methods(Collect)
		 */
		Map<String, List<Student>> st = list.stream().collect(Collectors.groupingBy(Student::getName));
		System.out.println("list of groups" + st.keySet());
		/**
		 * 9.Group both id and names(Collect)
		 */
		Map<Integer, Map<String, List<Student>>> group = list.stream()
				.collect(Collectors.groupingBy(Student::getId, Collectors.groupingBy(Student::getName)));
		System.out.println("combining id and names" + group);
		/**
		 * 10.Filter Method in names letter starts with "n"
		 */
		List<Student> fi = list.stream().filter(t -> t.getName().startsWith("N")).collect(Collectors.toList());
		System.out.println("names starts with N is:-" + fi);
		/**
		 * //11. demonstration of AllMatch method
		 */
		// boolean answer = stream.allMatch(str -> str.length() > 2);
		boolean answer = list.stream().allMatch(str -> str.getId() > 534);
		System.out.println(answer);
		/**
		 * 12.AnyMtach
		 */
		boolean answer1 = list.stream().anyMatch(c -> c.getMarks() > 8000);
		System.out.println(answer1);

		/**
		 * 13.
		 */

	}

}