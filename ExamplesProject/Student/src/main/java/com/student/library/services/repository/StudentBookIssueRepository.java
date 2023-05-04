package com.student.library.services.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.student.library.services.entity.StudentBookIssue;


public interface StudentBookIssueRepository extends JpaRepository<StudentBookIssue, Long> {

	

}
