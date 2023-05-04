package com.library.service.book.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.service.book.entity.RequestBook;
import com.library.service.book.entity.Student;



@Repository
public interface RequestRepository extends JpaRepository<RequestBook, Long> {
	
	Optional<RequestBook> findByIdAndStudent(Long requestBookId, Student student);
	Optional<RequestBook> findById(Long requestBookId);
	Optional<RequestBook> findBycardNo(String cardNo);
}
