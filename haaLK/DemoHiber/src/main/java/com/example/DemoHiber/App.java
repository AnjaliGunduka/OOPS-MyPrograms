package com.example.DemoHiber;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		Students s1 = new Students();// i want to persist yhe code in db
		s1.setId(534);
		s1.setSname("Anjali");
		s1.setMarks(80);

		Configuration com = new Configuration();
		SessionFactory sf = com.buildSessionFactory();

		Session session = sf.openSession();
		session.save(s1);

	}
}
