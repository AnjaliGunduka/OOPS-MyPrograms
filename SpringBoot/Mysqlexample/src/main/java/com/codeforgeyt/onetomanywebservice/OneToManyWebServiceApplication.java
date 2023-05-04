package com.codeforgeyt.onetomanywebservice;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.LocalDate;
@SpringBootApplication
public class OneToManyWebServiceApplication {
	public static void main(String[] args) throws IOException {
		SpringApplication.run(OneToManyWebServiceApplication.class, args);

		// create ArrayList list1
		ArrayList<String> list1 = new ArrayList<String>();

		// Add values in ArrayList
		list1.add("A");
		list1.add("B");
		list1.add("c");
		list1.add("s");

		// print list 1
		System.out.println("List1: " + list1);

		// Create ArrayList list2
		ArrayList<String> list2 = new ArrayList<String>();

		// Add values in ArrayList
		list2.add("A");
		list2.add("B");
		list2.add("Gaurav");

		// print list 2
		System.out.println("List2: " + list2);

		// Find common elements
		System.out.print("Common elements: ");
		System.out.println(list1.stream().filter(list2::contains).collect(Collectors.toList()));
		list1.addAll(list2);
		System.out.println(" duplicates are Removed : ");
		List<String> newList = list1.stream().distinct().collect(Collectors.toList());
		/** 
		 * Print the ArrayList with duplicates removed
		 */
				System.out.println("ArrayList with duplicates removed: " + newList);

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("enter date");
		String s = br.readLine();
		Date date = Date.valueOf(s);// converting string into sql date
		LocalDate s1 = date.toLocalDate();// convert date object to localdate
		System.out.println(s1.getDayOfWeek());
		}


	}

//	@Bean
//	public Docket api() {
//		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
//				.securityContexts(Arrays.asList(securityContext())).securitySchemes(Arrays.asList(apiKey())).select()
//				.apis(RequestHandlerSelectors.any())
//				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).paths(PathSelectors.any())
//				.build();
//	}
//
//	private ApiKey apiKey() {
//		return new ApiKey("JWT", "Authorization", "header");
//	}
//
//	private SecurityContext securityContext() {
//		return SecurityContext.builder().securityReferences(defaultAuth()).build();
//	}
//
//	private List<SecurityReference> defaultAuth() {
//		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//		authorizationScopes[0] = authorizationScope;
//		return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
//	}
//
//	private ApiInfo apiInfo() {
//		return new ApiInfo("Bank Service REST API", "Banks, Accounts and Transactions Management REST API.", "1.0",
//				"Terms of service", new Contact("Murali Krishna", "", "mkkasturi12@gmail.com"), "License of API",
//				"API license URL", Collections.emptyList());
//	}


