package GnericsExamples;

import java.util.TreeSet;

public class StudentCollge {
	public static void main(String[] args) {

		TreeSet<Student<String>> ts = new TreeSet<Student<String>>(new IdComp<String>());

		ts.add(new Student<String>("1", "Anjali"));

		ts.add(new Student<String>("5", "Neha"));

		ts.add(new Student<String>("3", "Anusha"));
		
		ts.add(new Student<String>("4", "Lalli"));
		
		ts.add(new Student<String>("6", "preethi"));
		
		for (Student<String> s : ts) {
			System.out.println(s);
		}
	}

}
