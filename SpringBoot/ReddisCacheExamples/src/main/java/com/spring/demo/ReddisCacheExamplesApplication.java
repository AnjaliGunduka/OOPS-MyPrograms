package com.spring.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ReddisCacheExamplesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReddisCacheExamplesApplication.class, args);
	}

}
