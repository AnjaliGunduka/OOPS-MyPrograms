package com.online.web.service;

import java.io.IOException;

import com.online.web.model.Cart;

public interface CartService {

	public void addCart(Cart cart);
	
	public Cart getCartById(long cartId);

	Cart validateCustomer(long cartId) throws IOException;
}
