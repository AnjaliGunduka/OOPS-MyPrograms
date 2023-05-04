package com.example.demo.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.Student;



@FeignClient(value = "springaop",url = "http://localhost:8088") 
public interface StudentFeignClient {

	@GetMapping(value = "/aop/student/{id}")
	public Student getStudent(int id);
	@GetMapping(value = "/aop/students")
	public List<Student> getStudents();

}
