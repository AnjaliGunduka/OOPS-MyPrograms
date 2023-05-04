package com.eureka.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PaymentEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentEurekaServerApplication.class, args);
	}

}
