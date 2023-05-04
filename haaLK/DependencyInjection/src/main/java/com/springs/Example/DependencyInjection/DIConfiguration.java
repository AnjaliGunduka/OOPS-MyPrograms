package com.springs.Example.DependencyInjection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages="com.springs.Example.DependencyInjection")
public class DIConfiguration {
//	@Bean
//	public MessageService getMessageService(){
//		return new EmailService();
//	}
}
