package StreamsApi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class EmployeeTest {
public static void main(String[] args) {
		 Employee e1 = new Employee(1l, "venkatesh", 15000d);
		 Employee e2 = new Employee(2l, "rajesh", 14000d);
		 Employee e3 = new Employee(3l, "ramesh", 20000d);
		 Employee e4 = new Employee(4l, "sravan", 5000d);
		 Employee e5 = new Employee(5l, "uday", 35000d);
		 Employee e6 = new Employee(6l, "srinu", 19000d);
		 List<Employee> employees = new ArrayList<Employee>();
		 employees.add(e1);
		 employees.add(e2);
		 employees.add(e3);
		 employees.add(e4);
		 employees.add(e5);
		 employees.add(e6);
		 // print the employee ids having salary > 15000 in asc order
		 Collection<Long> c = employees.stream().filter(e -> {
		 return e.sal > 15000d;
		 }).map(e -> e.getId()).collect(Collectors.toCollection(TreeSet::new));
		 System.out.println(c);
		 // print the employee ids having salary > 15000 in asc order
		 employees.stream().filter(e -> e.sal > 15000d).map(Employee::getId)
		 .collect(Collectors.toCollection(TreeSet::new)).forEach(System.out::println);
		 }
		}


