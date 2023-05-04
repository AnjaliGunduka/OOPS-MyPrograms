package com.payment.streams.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payment.streams.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findByName(String name);
}
