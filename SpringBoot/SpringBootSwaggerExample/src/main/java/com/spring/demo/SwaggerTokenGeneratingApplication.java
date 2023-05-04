package com.spring.demo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication

@EnableSwagger2
public class SwaggerTokenGeneratingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwaggerTokenGeneratingApplication.class, args);
	}
	/**
	 * Setting Swagger Path with @RestController Class Annotation and
	 * Enabled JWT Token Authorization globally for All APIs in Swagger UI
	 * 
	 * @return
	 */
	@Bean
	public Docket swaggerConfiguration() {
		// return instance od docket
		return new Docket(DocumentationType.SWAGGER_2).select().paths(PathSelectors.ant("/api/*"))
				.apis(RequestHandlerSelectors.basePackage("com.spring")).build().apiInfo(apiInfo());
	}

	private ApiKey apiKey() {
		return new ApiKey("JWT", "Authorization", "header");
	}
	private ApiInfo apiInfo() {
		return new ApiInfo("Address Details", "Banks, Accounts and Transactions Management REST API.", "1.0",
				"Terms of service", new Contact("Murali Krishna", "", "mkkasturi12@gmail.com"), "License of API",
				"API license URL", Collections.emptyList());
	}
	
	
}
