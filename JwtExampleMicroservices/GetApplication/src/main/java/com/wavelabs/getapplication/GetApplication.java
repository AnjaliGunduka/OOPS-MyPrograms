package com.wavelabs.getapplication;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
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
@EnableFeignClients
@EnableSwagger2
public class GetApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetApplication.class, args);
	}
	
	
	@Bean
	public Docket swaggerConfiguration() {

		return new Docket(DocumentationType.SWAGGER_12).select().paths(PathSelectors.ant("/api/*"))
				.apis(RequestHandlerSelectors.basePackage("com.wavelabs")).build().apiInfo(apiInfo());

	}
	
	private ApiInfo apiInfo() {
		return new ApiInfo("crud mysql example", "Banks, Accounts and Transactions Management REST API.", "1.0",
				"Terms of service", new Contact("Murali krishna", "", "mk@gmail.com"), "License of API",
				"API license URL", Collections.emptyList());
	}

}
