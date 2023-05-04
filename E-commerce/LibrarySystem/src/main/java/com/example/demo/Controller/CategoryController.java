package com.example.demo.Controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Category;
import com.example.demo.Request.CategoryRequest;
import com.example.demo.service.CategoryService;

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
			@ApiResponse(code = HttpServletResponse.SC_OK, response = CategoryRequest.class, message = "Book created Successfully"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, response = String.class, message = "Invalid parameters") })
	@PostMapping(value = "/Categorycreate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CategoryRequest> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
		Category createdCategory = categoryService.createCategory(categoryRequest);
		ModelMapper modelMapper = new ModelMapper();
		return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(createdCategory, CategoryRequest.class));
	}
	
	
	
}
