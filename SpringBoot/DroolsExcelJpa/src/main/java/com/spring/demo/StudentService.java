package com.spring.demo;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class StudentService {
	@Autowired
    private KieContainer kieContainer;
	@Autowired
	StudentRepository studentRepository;
	public Student getStudent(Student student) {
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.insert(student);
        kieSession.fireAllRules();
        kieSession.dispose();    
        return studentRepository.save(student);
    }

	
}
