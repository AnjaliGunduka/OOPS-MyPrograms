package com.example.stream.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.stream.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {

	
}
