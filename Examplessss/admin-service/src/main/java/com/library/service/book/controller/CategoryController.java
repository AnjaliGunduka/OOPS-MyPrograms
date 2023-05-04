package com.library.service.book.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.service.book.entity.Category;
import com.library.service.book.service.CategoryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/bookService/category")
public class CategoryController {
	@Autowired
	CategoryService categoryService;
	
	@ApiOperation(value = "Creates a Category")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, response = Category.class, message = "Book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, response = String.class, message = "Invalid Token / Without Token"),
			@ApiResponse(code = HttpServletResponse.SC_FORBIDDEN, response = String.class, message = "UnAuthorized Access") })
	@PostMapping(value = "/Categorycreate")
	public Category createCategory(@RequestBody Category category) {
		return  categoryService.createCategory(category);
	}
	
	
	
}
