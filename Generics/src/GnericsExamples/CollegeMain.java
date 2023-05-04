package GnericsExamples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CollegeMain {
	public static <T> void main(String[] args) {
		List<College<String>> list = new ArrayList<College<String>>();
		College std1 = new College(534, "Anjali", "cse");
		College std2 = new College(531, "Gajjela Neha", "Cse");
		College std3 = new College(513, "Lalitha", "Cse");
		College std4 = new College(510, "Preethi", "Cse");
		College std5 = new College(550, "Nirosha", "Cse");
		list.add(std1);
		list.add(std2);
		list.add(std3);
		list.add(std4);
		list.add(std5);
//		Set<String>a=list.stream().filter(e->{
//			return e.stuId>530;
//		});
		 
		
		
		for (College<String> display : list) {

			System.out.println(display);
		}
	}

	public static <E> void dispaly(E[] elements) {
		for (E element : elements) {
			System.out.println(element);
		}
	}
}