package com.javatechie.jpa.repository;

import com.javatechie.jpa.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
	Page<Product> findByPublished(boolean published, Pageable pageable);

	Page<Product> findByTitleContaining(String title, Pageable pageable);

	Product findByDocName(String fileName);
}
