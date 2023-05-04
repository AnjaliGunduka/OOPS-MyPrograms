package com.online.web.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.web.model.Cart;
import com.online.web.model.CartItem;
import com.online.web.model.Product;

@Repository

public interface CartItemDao extends CrudRepository<CartItem, Long>{

	List<CartItem> findUserByProduct(Product product);
	
	void removeByCart(Cart cart);
	
	List<CartItem> findAllCartItemsBycart(Cart cart);
	
	@Modifying
	@Transactional
	@Query(value="DELETE  FROM cartItem  WHERE cartItemId = :cartItemId ",nativeQuery=true)
    void deleteCartItemById(@Param("cartItemId")long cartItemId);
	
	
	
	
}
