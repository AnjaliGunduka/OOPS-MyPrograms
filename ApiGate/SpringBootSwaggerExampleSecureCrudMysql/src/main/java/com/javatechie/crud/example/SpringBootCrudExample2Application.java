package com.javatechie.crud.example;

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
public class SpringBootCrudExample2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootCrudExample2Application.class, args);
	}

	private ApiKey apiKey() {
		return new ApiKey("JWT", "Authorization", "header");
	}
	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
	}
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
				.securityContexts(Arrays.asList(securityContext())).securitySchemes(Arrays.asList(apiKey())).select()
				.apis(RequestHandlerSelectors.any())
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).paths(PathSelectors.any())
				.build();
	}
	private ApiInfo apiInfo() {
		return new ApiInfo("crud mysql example", "Banks, Accounts and Transactions Management REST API.", "1.0",
				"Terms of service", new Contact("Murali krishna", "", "mk@gmail.com"), "License of API",
				"API license URL", Collections.emptyList());
	}
	
}
