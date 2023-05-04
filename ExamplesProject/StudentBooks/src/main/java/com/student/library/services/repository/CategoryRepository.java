package com.student.library.services.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.student.library.services.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

}
