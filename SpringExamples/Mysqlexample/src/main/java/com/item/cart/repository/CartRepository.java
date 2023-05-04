package com.item.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.item.cart.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
