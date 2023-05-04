package com.library.service.book.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.service.book.entity.Category;
import com.library.service.book.repository.CategoryRepository;


@Service
public class CategoryService {
	private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
	@Autowired
	CategoryRepository categoryRepository;

	/**
	 * 
	 * @param category
	 * @return
	 */

	public Category createCategory(Category category) {
		log.info("Category Created");
		return categoryRepository.save(category);
	}

}
