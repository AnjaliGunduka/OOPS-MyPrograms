package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {

}
