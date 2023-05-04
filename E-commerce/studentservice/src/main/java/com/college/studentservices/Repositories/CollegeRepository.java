package com.college.studentservices.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.college.studentservices.entities.College;

@Repository
public interface CollegeRepository extends JpaRepository<College, Long> {
	Optional<College> findById(Long collegeId);
}
