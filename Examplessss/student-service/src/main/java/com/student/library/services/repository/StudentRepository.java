package com.student.library.services.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.student.library.services.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	Optional<Student> findById(Long studentId);

	Student findByEmail(String userNameOremail);

	Student findByUserName(String userNameOremail);

	Student findByCardNo(String userNameOrcardNo);

	

}
