package com.codeforgeyt.onetomanywebservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codeforgeyt.onetomanywebservice.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
