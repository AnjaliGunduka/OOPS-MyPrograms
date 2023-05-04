package GnericsExamples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComparableMain {
public static void main(String[] args) {
	List<ComparableExample> students=new ArrayList<ComparableExample>();
	students.add(new ComparableExample(534,"Anjali","Cse"));
	students.add(new ComparableExample(531,"G.Neha","Cse"));
	students.add(new ComparableExample(513,"Lalitha","Cse"));
	students.add(new ComparableExample(510,"Preethi","Cse"));
	students.add(new ComparableExample(550,"Nirosha","Cse"));
           
	Collections.sort(students);
	
	
	for(ComparableExample com:students)
	{
		System.out.println(com);
	}	
}
}
