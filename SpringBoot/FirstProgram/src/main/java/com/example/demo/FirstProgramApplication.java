package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FirstProgramApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context=	SpringApplication.run(FirstProgramApplication.class, args);
//		Student s=new Student();//manually creating a object
		Student s =context.getBean(Student.class);//if we comment this also will get the output
		s.show();
		
		
	}

}
