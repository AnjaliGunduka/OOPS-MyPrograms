package com.javatechie.jpastreamer.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javatechie.jpastreamer.entity.Employee;
import com.javatechie.jpastreamer.repository.EmployeeRepository;
import com.speedment.jpastreamer.application.JPAStreamer;
import com.javatechie.jpastreamer.entity.Employee$;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository repository;

	@Autowired
	private JPAStreamer jpaStreamer;

	public List<Employee> saveEmployees(List<Employee> employees) {
		return repository.saveAll(employees);
	}

	public Employee minPaidEmp() {
		return jpaStreamer.stream(Employee.class).min(Comparator.comparing(Employee::getSalary)).get();
	}

	

	public Map<String, List<Employee>> getEmployeeGroupByDept() {
		return jpaStreamer.stream(Employee.class).collect(Collectors.groupingBy(Employee::getDept));
	}

	

}
