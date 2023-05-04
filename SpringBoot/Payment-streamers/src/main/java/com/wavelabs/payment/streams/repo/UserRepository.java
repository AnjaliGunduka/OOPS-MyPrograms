package com.wavelabs.payment.streams.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wavelabs.payment.streams.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
