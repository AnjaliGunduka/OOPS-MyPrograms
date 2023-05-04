package com.library.service.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.service.book.entity.Category;
@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

}
