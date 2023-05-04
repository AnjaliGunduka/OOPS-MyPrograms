package com.javainuse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.javainuse.data.EmployeeRepository;
import com.javainuse.model.Employee;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeRepository employeeData;

	@RequestMapping(value = "/listPageable", method = RequestMethod.GET)
	Page<Employee> employeesPageable(Pageable pageable) {
		return employeeData.findAll(pageable);

	}

}
