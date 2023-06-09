package com.mvc.Login;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan({"com.mvc.Login"})
public class LoginConfig {
	@Bean
	 public InternalResourceViewResolver viewResolver()
	 {
		 InternalResourceViewResolver vr=new InternalResourceViewResolver();
		 vr.setPrefix("/");
		 vr.setSuffix(".jsp");
		 return vr;
	 }
}
