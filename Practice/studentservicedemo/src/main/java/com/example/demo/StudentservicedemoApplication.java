package com.example.demo;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;


@EnableFeignClients
@SpringBootApplication
public class StudentservicedemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentservicedemoApplication.class, args);
	}
	
}
