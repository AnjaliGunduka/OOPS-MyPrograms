package com.spring.demo;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class MarksController {
	@Autowired
	private KieSession session;
	@PostMapping("/marks")
	public Student studentMarksNow(@RequestBody  Student order) {
		session.insert(order);
		session.fireAllRules();
		return order;
	}

}
