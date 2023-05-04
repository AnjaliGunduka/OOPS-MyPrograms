package com.employee.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.employee.model.Employee;
import com.employee.model.EmployeePage;
import com.employee.model.EmployeeSearchCriteria;
import com.employee.repository.EmployeeCriteriaRepository;
import com.employee.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeCriteriaRepository employeeCriteriaRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeeCriteriaRepository employeeCriteriaRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeCriteriaRepository = employeeCriteriaRepository;
    }

    public Page<Employee> getEmployees(EmployeePage employeePage,
                                       EmployeeSearchCriteria employeeSearchCriteria){
        return employeeCriteriaRepository.findAllWithFilters(employeePage, employeeSearchCriteria);
    }

    public Employee addEmployee(Employee employee){
        return employeeRepository.save(employee);
    }
}
