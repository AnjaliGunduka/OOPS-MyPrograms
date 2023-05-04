package com.student.library.services.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.student.library.services.entity.RequestBook;
import com.student.library.services.entity.Student;
@Repository
public interface RequestRepository extends JpaRepository<RequestBook, Long> {
	
	Optional<RequestBook> findByIdAndStudent(Long requestId, Student student);	
}
