package com.example.stream.service;

import com.example.stream.entity.Employee;
import com.example.stream.repository.EmployeeRepository;
import com.example.stream.service.excepions.ResourceNotFoundException;
import com.example.stream.entity.Employee$;
import com.speedment.jpastreamer.application.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository emprepository;

	@Autowired
	private JPAStreamer jpaStreamer;
	/*
	 * 1. saving a specific record by using the method save() of JpaRepository
	 */

	public Employee saveEmployees(Employee employees) {
		return emprepository.save(employees);
	}

	public List<Employee> getEmployees() {
		return jpaStreamer.stream(Employee.class).collect(Collectors.toList());
	}

	/*
	 * Stream(From) filter(Where)(Before collectiong) Collect(Group by) map (select)
	 * flatmap(join) Sorted(orderby) filter(Having) AfterCollecting
	 */
	public List<Employee> getEmployeesByDept(String dept) {
		return jpaStreamer.stream(Employee.class).filter(Employee$.dept.equal(dept)).collect(Collectors.toList());
	}

	public List<Employee> getEmployees(int offset, int limit) {
		return jpaStreamer.stream(Employee.class).sorted(Employee$.name).skip(offset).limit(limit)
				.collect(Collectors.toList());
	}

	public List<Employee> getEmployeesByDeptAndSalary(String dept, double salary) {
		return jpaStreamer.stream(Employee.class)
				.filter(Employee$.dept.equal(dept).and(Employee$.salary.greaterOrEqual(salary)))
				.collect(Collectors.toList());
	}

	public List<Employee> getEmployeeBySalaryRange(double salary1, double salary2) {
		return jpaStreamer.stream(Employee.class).filter(Employee$.salary.between(salary1, salary2))
				.collect(Collectors.toList());
	}

	// aggregate fun

	public Employee minPaidEmp() {
		return jpaStreamer.stream(Employee.class).min(Comparator.comparing(Employee::getSalary)).get();
	}

	public List<Employee> getEmployeesByIds(List<Integer> ids) {
		return jpaStreamer.stream(Employee.class).filter(Employee$.id.in(ids)).collect(Collectors.toList());

	}

	public Map<String, List<Employee>> getEmployeeGroupByDept() {
		return jpaStreamer.stream(Employee.class).collect(Collectors.groupingBy(Employee::getDept));
	}

}
