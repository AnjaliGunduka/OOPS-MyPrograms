package com.student.library.services.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.student.library.services.entity.RequestBook;
import com.student.library.services.entity.Student;
import com.student.library.services.enums.RequestStatus;

@Repository
public interface RequestRepository extends JpaRepository<RequestBook, Long> {
	
	Optional<RequestBook> findByIdAndStudent(Long requestBookId, Student student);
	Optional<RequestBook> findById(Long requestBookId);
	Optional<RequestBook> findBycardNo(String cardNo);
	List<RequestBook> getBookById(Long requestBookId);
	RequestBook findByStudentId(Long studentId);
	List<RequestBook> findByStudentIdAndStatus(Long studentId, RequestStatus status);
	
	
	
}
