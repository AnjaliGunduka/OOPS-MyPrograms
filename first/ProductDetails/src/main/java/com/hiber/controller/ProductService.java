package com.hiber.controller;

import java.util.List;

import com.hiber.model.Product;

public interface ProductService {
	List  getAllProducts();
	 
	 boolean addProduct(Product product);

	  boolean deleteProduct(int productId);
}
