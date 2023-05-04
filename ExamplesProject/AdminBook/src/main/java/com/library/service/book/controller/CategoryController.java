package com.library.service.book.controller;

import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.service.book.entity.Category;
import com.library.service.book.request.CategoryRequest;
import com.library.service.book.response.CategoryResponse;
import com.library.service.book.service.CategoryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/library-services/category")
public class CategoryController {
	@Autowired
	CategoryService categoryService;
	
	@ApiOperation(value = "Creates a Category")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = CategoryResponse.class, message = "Book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@PostMapping(value = "/Categorycreate")
	public CategoryResponse createCategory( @RequestBody CategoryRequest categoryRequest) {
		Category createdCategory = categoryService.createCategory(categoryRequest);
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(createdCategory, CategoryResponse.class);
	}
	
	
	
}
