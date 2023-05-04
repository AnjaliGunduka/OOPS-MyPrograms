package com.demo.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
	public static void main(String[] args) {
		ApplicationContext factory = new AnnotationConfigApplicationContext(AppConfig.class);

		Oneplus nord = factory.getBean(Oneplus.class);
		nord.config();
	}
}
