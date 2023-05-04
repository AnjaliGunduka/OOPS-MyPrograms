package com.library.example.demo.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.example.demo.Entity.Category;
import com.library.example.demo.Repository.CategoryRepository;
import com.library.example.demo.Request.CategoryRequest;

@Service
public class CategoryService {
	private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
	@Autowired
	CategoryRepository categoryRepository;
	
	
	/**
	 * 
	 * Creates Category and saves into the Map
	 * 
	 * CategoryRequest
	 * @return createdCategory
	 *
	 */
	
	public Category createCategory(CategoryRequest categoryRequest) {
		ModelMapper modelMapper = new ModelMapper();
		Category category = modelMapper.map(categoryRequest, Category.class);
		log.info("Creating Category");
		Category createdCategory = categoryRepository.save(category);
		log.info("Category created");
		return createdCategory;
	}
	
	
	
	
	
	
}
