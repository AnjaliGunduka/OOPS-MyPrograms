package com.example.stream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.stream.entity.Employee;
import com.example.stream.service.EmployeeService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeservice;

    @PostMapping
    public Employee saveEmployees(@RequestBody Employee employees){
        return employeeservice.saveEmployees(employees);
    }

     @GetMapping("/{offset}/{limit}")
    public List<Employee> getEmployees(@PathVariable int offset,@PathVariable int limit) {
        return employeeservice.getEmployees(offset,limit);
    }

    @GetMapping("/{dept}")
    public List<Employee> getEmployeesByDept(@PathVariable String dept) {
        return employeeservice.getEmployeesByDept(dept);
    }

    @GetMapping("/fetch/{dept}/{salary}")
    public List<Employee> getEmployeesByDeptAndSalary(@PathVariable String dept,@PathVariable double salary) {
        return employeeservice.getEmployeesByDeptAndSalary(dept,salary);
    }

    @GetMapping("/range/{sal1}/{sal2}")
    public List<Employee> getEmployeesBySalaryRange(@PathVariable double sal1,@PathVariable double sal2) {
        return employeeservice.getEmployeeBySalaryRange(sal1, sal2);
    }

    @GetMapping("/min")
    public Employee getLessPaidEmployee(){
        return employeeservice.minPaidEmp();
    }

    @PostMapping("/ids")
    public  List<Employee> getEmployeesByIds(@RequestBody List<Integer> ids){
        return employeeservice.getEmployeesByIds(ids);
    }

    @GetMapping("/groupByDept")
    public Map<String, List<Employee>> getEmployeeGroupByDept(){
        return employeeservice.getEmployeeGroupByDept();
    }
   
}
