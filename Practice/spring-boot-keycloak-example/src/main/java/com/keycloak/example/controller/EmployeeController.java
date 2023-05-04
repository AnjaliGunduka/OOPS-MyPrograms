package com.keycloak.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.keycloak.example.model.Employee;
import com.keycloak.example.repository.EmployeeRepository;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    @PostMapping("/admin/employee")
    @RolesAllowed("admin")
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee){
        return ResponseEntity.ok(employeeRepository.save(employee));
    }

    @GetMapping("/user/employee")
    @RolesAllowed({"employee","admin"})
    public ResponseEntity<List<Employee>> getEmployee(){
        return ResponseEntity.ok(employeeRepository.findAll());
    }
}
