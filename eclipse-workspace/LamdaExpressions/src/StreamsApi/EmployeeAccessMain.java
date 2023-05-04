package StreamsApi;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAccessMain {
	public static void main(String[] args) {
		EmployeAccess s = new EmployeAccess(534, "Anjali", 30000);
		EmployeAccess s1 = new EmployeAccess(531, "Neha", 50000);
		EmployeAccess s2 = new EmployeAccess(513, "Lalitha", 60000);
		EmployeAccess s3 = new EmployeAccess(531, "Nirosha", 80000);
		EmployeAccess s5 = new EmployeAccess(510, "Preethi", 50000);
		List<EmployeAccess> list = new ArrayList<EmployeAccess>();
		list.add(s);
		list.add(s1);
		list.add(s2);
		list.add(s3);
		list.add(s5);
		list.stream().filter(EmployeAccess -> EmployeAccess.id == 534)
		.forEach(EmployeAccess -> System.out.println("name of student:-" + EmployeAccess.name));
		list.stream().filter(Student -> Student.id == 534)
		.forEach(EmployeAccess -> System.out.println("name of student:-" + EmployeAccess.sal));


		
		
		
	}
}
