package com.college.studentservices.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.college.studentservices.entities.College;
import com.college.studentservices.entities.Student;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	Optional<Student> findByIdAndCollege(Long studentId, College college);	
	
}
