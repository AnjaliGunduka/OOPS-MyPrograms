package com.wavelabs.postapplication;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



@RequestMapping("/app2")
@RestController
public class EmployeeController {
	
	@RequestMapping(value = "/employee/save", method = RequestMethod.POST)
	@ResponseBody
	public Employee save(@RequestBody Employee employee) {
		System.out.println(employee.toString());
//		employee.getName();
		return employee;
	}
	@PostMapping("/addemployee")
    public Employee addProduct(@RequestBody Employee employee) {
         employee.getId();
         employee.getName();
         employee.getEmail();
         return employee;
        
    }
}
