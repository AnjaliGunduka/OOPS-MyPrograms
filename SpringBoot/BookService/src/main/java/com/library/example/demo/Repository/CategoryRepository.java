package com.library.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.example.demo.Entity.Category;
@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

}
