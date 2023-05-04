package com.wavelabs.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wavelabs.dao.StudentDao;
import com.wavelabs.model.Student;

@Service
public class StudentService {
	@Autowired
	StudentDao studentDao;

	@Transactional
	public List<Student> getAllStudents() {
		return studentDao.getAllStudents();
	}

	@Transactional
	public Student getStudent(String studId) {
		return studentDao.getStudent(studId);
	}

	@Transactional
	public void addStudent(Student Student) {
		studentDao.addStudent(Student);
	}

	@Transactional
	public void updateStudent(Student Student) {
		studentDao.updateStudent(Student);

	}

	@Transactional
	public void deleteStudent(String studId) {
		studentDao.deleteStudent(studId);
	}
}