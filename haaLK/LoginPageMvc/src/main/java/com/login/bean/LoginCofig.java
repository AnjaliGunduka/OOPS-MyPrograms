package com.login.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
@ComponentScan({"com.login.bean"})
@Controller
public class LoginCofig {
@Bean
public InternalResourceViewResolver viewResolve()
{
	InternalResourceViewResolver ir=new InternalResourceViewResolver();
	return ir;
}
}
