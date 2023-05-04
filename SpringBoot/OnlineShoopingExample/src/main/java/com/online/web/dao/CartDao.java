package com.online.web.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.web.model.Cart;

@Repository

public interface CartDao extends CrudRepository<Cart, Long> {

	Cart findUserBycartId(long cartId);

	Cart findOne(long cartId);
}
