package com.library.service.book.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.service.book.entity.Student;



@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	Optional<Student> findById(Long studentId);

	Optional<Student> findBycardNo(String cardNo);

	Boolean findByuserName(String userName);

	Boolean findByemail(String email);

}
