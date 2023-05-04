package com.spring.demo;

import java.util.function.Function;
import java.util.function.Predicate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Java8StreamsApplication {

	public static void main(String[] args) {
		SpringApplication.run(Java8StreamsApplication.class, args);
		// Square Root Using Functional Interface
		Function<Integer, Double> squareRoot = (a) -> {
			return Math.sqrt(a);
		};
		int a = 4;
		double r = squareRoot.apply(a);
		System.out.println(r);

		// Square Root Multiplication Using Functional Interface
		// lambda expression to define the calculate method
		Multiplication s = (int x) -> x * x;
		// parameter passed and return type must be
		// same as defined in the prototype
		int ans = s.calculate(a);
		System.out.println(ans);
		
	}
}
@FunctionalInterface
 interface Multiplications<T> {
	public T multiply(T val1, T val2);
}
interface Multiplication {
	int calculate(int x);

}
