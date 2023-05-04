package com.logtrace.demo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import brave.sampler.Sampler;
@SpringBootApplication
public class Microservice2Application {

	public static void main(String[] args) {
		SpringApplication.run(Microservice2Application.class, args);
	}
	@RestController
	class Microservice2Controller {

		private final Logger LOG = LoggerFactory.getLogger(this.getClass());

		@Autowired
		RestTemplate restTemplate;

		@Bean
		public RestTemplate getRestTemplate() {
			return new RestTemplate();
		}
		@Bean
		public Sampler defaultSampler() {
			return Sampler.ALWAYS_SAMPLE;
		}
		@GetMapping(value = "/microservice2")
		public String method2() {
			LOG.info("Inside method2");
			String baseUrl = "http://localhost:8083/microservice3";
			String response = (String) restTemplate.exchange(baseUrl, HttpMethod.GET, null, String.class).getBody();
			LOG.info("The response received by method2 is " + response);
			return response;
		}
	}
}
