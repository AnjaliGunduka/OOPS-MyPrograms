package com.jdbc.hiber.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan({"com.jdbc.hiber"})
public class EmployeeConfig {
	@Bean
	 public InternalResourceViewResolver viewResolver()
	 {
		 InternalResourceViewResolver vr=new InternalResourceViewResolver();
		 vr.setPrefix("/");
		 vr.setSuffix(".jsp");
		 return vr;
	 }
}
