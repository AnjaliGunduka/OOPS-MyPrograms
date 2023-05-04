package com.library.service.book.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.service.book.entity.Category;
import com.library.service.book.repository.CategoryRepository;
import com.library.service.book.request.CategoryRequest;

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
		log.info("Creating Category");//will give information 
		Category createdCategory = categoryRepository.save(category);//that request is inserted and then returned
		log.info("Category created");
		return createdCategory;
	}
	
	
	
	
	
}
