package com.item.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.item.cart.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
