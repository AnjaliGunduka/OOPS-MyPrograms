package com.springs.jdbc.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.springs.jdbc.Controller.UserDao;
import com.springs.jdbc.Controller.UserDaoImpl;

@Configuration
@EnableWebMvc
@ComponentScan("com.springs.jdbc")
public class WebMvcConfig {
	@Bean
	InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver vr = new InternalResourceViewResolver();
		vr.setPrefix("/");
		vr.setSuffix(".jsp");
		return vr;

	}
	@Bean
	DriverManagerDataSource getDataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/springJdbc");
		ds.setUsername("root");
		ds.setPassword("Tiger");
		return ds;

	}

	@Bean
	public UserDao getUserDao() {
		return new UserDaoImpl(getDataSource());
	}

}
