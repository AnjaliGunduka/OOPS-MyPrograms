package com.wavelabs.employee.stream.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wavelabs.employee.stream.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
}
