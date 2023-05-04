package StreamsApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class DepartmentMain {
public static void main(String[] args) {
	List<Department> ts = new ArrayList<Department>();//(new IdComp());

	ts.add(new Department(1, "Anjali",800));

	ts.add(new Department(5, "Neha",700));

	ts.add(new Department(3, "Anusha",500));
	
	ts.add(new Department(4, "Lalli",300));
	
	ts.add(new Department(6, "preethi",800));

	Collections.sort(ts);
	
	for (Department s : ts) {
		if(s.marks>500)
		{
			System.out.println(s);
		}
		
}
}
}
