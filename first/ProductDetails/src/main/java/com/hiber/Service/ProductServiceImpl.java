package com.hiber.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hiber.controller.ProductService;
import com.hiber.dao.ProductDAO;
import com.hiber.model.Product;
@Service("ps")
public class ProductServiceImpl implements ProductService{
	@Autowired
	ProductDAO productDAO;
	public List getAllProducts() {
		return productDAO.getAllProducts();
 
	}
 
	public boolean addProduct(Product product) {
		productDAO.addProduct(product);
		return true;
	}
 
	public boolean deleteProduct(int productId) {
		return productDAO.deleteProduct(productId);
 
	}
}
