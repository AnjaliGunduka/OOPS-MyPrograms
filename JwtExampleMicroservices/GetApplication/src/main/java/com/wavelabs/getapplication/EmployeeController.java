package com.wavelabs.getapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



@RequestMapping("/app1")
@RestController
public class EmployeeController {
	@Autowired
	RestClientService restClientService;

	@RequestMapping(value = "/employee/save", method = RequestMethod.GET)
	@ResponseBody
	public Employee save() {
		Employee employee = new Employee();
		employee.getId();
		employee.getName();
		employee.getEmail();
		employee.getPassword();
		return restClientService.save(employee);
	}
	 @PostMapping("/addemployee")
	    public Employee addProduct(@RequestBody Employee employee) {
	        return restClientService.save(employee);
	    }

	
}