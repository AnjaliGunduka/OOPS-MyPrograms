package com.spring.demo.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.demo.entity.Tutorials;

public interface TutorialRepository extends JpaRepository<Tutorials, Long> {
	Page<Tutorials> findByPublished(boolean published, Pageable pageable);

	Page<Tutorials> findByTitleContaining(String title, Pageable pageable);

	Tutorials findByDocName(String fileName);
}
