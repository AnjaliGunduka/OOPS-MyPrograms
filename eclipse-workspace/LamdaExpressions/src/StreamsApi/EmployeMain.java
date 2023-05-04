package StreamsApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class EmployeMain {
	public static void main(String[] args) {
		Employe e1 = new Employe(1, "venkatesh", 15000d);
		Employe e2 = new Employe(2, "rajesh", 14000d);
		Employe e3 = new Employe(3, "ramesh", 20000d);
		Employe e4 = new Employe(4, "sravan", 5000d);
		Employe e5 = new Employe(5, "uday", 35000d);
		Employe e6 = new Employe(6, "srinu", 19000d);
		List<Employe> employees = new ArrayList<Employe>();
		employees.add(e1);
		employees.add(e2);
		employees.add(e3);
		employees.add(e4);
		employees.add(e5);
		employees.add(e6);
		
		Comparator<Employe> a=new Comparator<Employe>()
		{

			@Override
			public int compare(Employe o1, Employe o2) {
				// TODO Auto-generated method stub
				 if(o1.getId()>o2.getId())
					 return 1;
				 else
					 return -1;
			}
	
		};
		System.out.println(employees);
		
		

	}
}
