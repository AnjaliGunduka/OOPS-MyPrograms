package com.wavelabs.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wavelabs.model.Student;

@Repository
public class StudentDao {
	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	public List<Student> getAllStudents() {
		Session session = this.sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Student> StudentList = session.createQuery("from Student").list();
		return StudentList;
	}

	public Student getStudent(String studId) {
		Session session = this.sessionFactory.getCurrentSession();
		Student student = (Student) session.get(Student.class, studId);
		return student;
	}

	public Student addStudent(Student Student) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(Student);
		return Student;
	}

	public void updateStudent(Student Student) {
		Session session = this.sessionFactory.getCurrentSession();
		session.saveOrUpdate(Student);
	}

	public void deleteStudent(String studId) {
		Session session = this.sessionFactory.getCurrentSession();
		Student s = (Student) session.load(Student.class, studId);
		if (null != s) {
			session.delete(s);
		}
	}
}
