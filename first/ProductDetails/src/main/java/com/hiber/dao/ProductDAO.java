package com.hiber.dao;

import java.util.List;

import com.hiber.model.Product;

public interface ProductDAO {
	 List  getAllProducts();
	 
	 boolean addProduct(Product product);
 
	  boolean deleteProduct(int productId);
}
