package com.stackfortech.redispubsub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RedisPubSubApplication {

	@GetMapping("/")
	public String home()
	{
		return "Welcome to AWS fIRST Program";
	}
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(RedisPubSubApplication.class, args);
	}

}
