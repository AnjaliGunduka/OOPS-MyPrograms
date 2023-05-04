package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
	@Autowired
	StudentRepository studentRepository;

	public Student createStudent(Student s) {
		return studentRepository.save(s);
	}

	public Optional<Student> getStudent(String email) {
		return studentRepository.findByEmailId(email);
	}

	public Optional<Student> deleteStudent(int id) {
		return studentRepository.findById(id);
	}

}
