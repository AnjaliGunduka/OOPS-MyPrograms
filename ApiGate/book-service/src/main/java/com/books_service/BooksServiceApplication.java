package com.books_service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
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
public class BooksServiceApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(BooksServiceApplication.class);

	@Autowired
	private Environment environment;

	@Override
	public void run(String... args) throws Exception {
		logger.info("\n----Begin logging BooksServiceApplication----");

		logger.info("----System Properties from VM Arguments----");
		logger.info("server.port: " + System.getProperty("server.port"));
		logger.info("----Program Arguments----");
		for (String arg : args) {
			logger.info(arg);
		}

		if (environment != null) {
			getActiveProfiles();
			logger.info("----Environment Properties----");
			logger.info("server.port: " + environment.getProperty("server.port"));
			logger.info("nl.amis.environment: " + environment.getProperty("nl.amis.environment"));
			logger.info("spring.datasource.url: " + environment.getProperty("spring.datasource.url"));
			logger.info("spring.datasource.username: " + environment.getProperty("spring.datasource.username"));
			logger.info("spring.datasource.password: " + environment.getProperty("spring.datasource.password"));
			logger.info("spring.jpa.database-platform: " + environment.getProperty("spring.jpa.database-platform"));
			logger.info("spring.jpa.hibernate.ddl-auto: " + environment.getProperty("spring.jpa.hibernate.ddl-auto"));
		}

		logger.info("----End logging BooksServiceApplication----");
	}

	private void getActiveProfiles() {
		for (final String profileName : environment.getActiveProfiles()) {
			logger.info("Currently active profile - " + profileName);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(BooksServiceApplication.class, args);
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
		return new ApiInfo("Books Management", "In this project we are Managing different types of books.", "1.0",
				"Terms of service", new Contact("Anjali", "", "anjali@gmail.com"), "License of API",
				"API license URL", Collections.emptyList());
	}

}
