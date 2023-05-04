package com.jdbc.hiber.config;

import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.jdbc.hiber.usersclass.User;

public class HibernateUtil {
	private static final SessionFactory concreteSessionFactory;
	static {
		try {
			Properties prop= new Properties();
			prop.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/springJdbc");
			prop.setProperty("hibernate.connection.username", "root");
			prop.setProperty("hibernate.connection.password", "Tiger");
			prop.setProperty("dialect", "org.hibernate.dialect.MySQLDialect");
			
			concreteSessionFactory = new AnnotationConfiguration().addPackage("com.jdbc.hiber.config")
				   .addProperties(prop)
				   .addAnnotatedClass(User.class)
				   .buildSessionFactory();
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
		
	}
	public static Session getSession()
			throws HibernateException {
		return concreteSessionFactory.openSession();
	}	
	public static void main(String... args){
		Session session=getSession();
		session.beginTransaction();
		User user=(User)session.get(User.class, new Integer(1));
		System.out.println(user.getName());
		session.close();
	}
	}
		
	

