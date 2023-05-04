package GnericsExamples;

import java.util.HashMap;

public class CollegeMapMain {
public static void main(String[] args) {
	CollegeMap<String, Integer> a=new CollegeMap<String, Integer>();
	a.add("Anjali", 534);
	a.getStudnames();
	System.out.println(a);
	HashMap<Integer,String> a1=new HashMap<Integer,String>();
	a1.put(534, "Anjali");
	System.out.println(a1);
	
	
}
}
