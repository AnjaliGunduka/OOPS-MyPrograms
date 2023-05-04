package com.kb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kb.dao.EmployeeDAO;
 
import com.kb.model.Employee;
 
@Service
public class EmployeeServiceImpl implements EmployeeService {
   
    @Autowired
    private EmployeeDAO employeeDAO;
 
    public void setemployeeDAO(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }
 
   
    @Transactional
    public void addEmployee(Employee employee) {
        employeeDAO.addEmployee(employee);
    }
 
   
    @Transactional
    public void updateEmployee(Employee employee) {
        employeeDAO.updateEmployee(employee);
    }
 
   
    @Transactional
    public List<Employee> listEmployees() {
        return this.employeeDAO.listEmployees();
    }
 
    
    @Transactional
    public Employee getEmployeeById(int id) {
        return employeeDAO.getEmployeeById(id);
    }
 
 
    @Transactional
    public void removeEmployee(int id) {
        employeeDAO.removeEmployee(id);
    }
}
