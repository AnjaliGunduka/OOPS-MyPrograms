package com.bookstore.service.prices.respositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.service.prices.entities.Price;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    Optional<Price> findByBookId(long bookId);
}
