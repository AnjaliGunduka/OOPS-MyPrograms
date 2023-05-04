package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LambdaExpresionsApplication {

	public static void main(String[] args) {

		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		/**
		 * 1.Using LambdaExpressions
		 * 
		 */
		//list.stream().count();
		list.forEach(n->System.out.println(list));
		//System.out.println(list);

		SpringApplication.run(LambdaExpresionsApplication.class, args);
	}

}
