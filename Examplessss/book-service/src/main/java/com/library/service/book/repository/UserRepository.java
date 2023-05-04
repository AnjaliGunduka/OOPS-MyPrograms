package com.library.service.book.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.service.book.entity.ApplicationUser;



@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Long> {

	Optional<ApplicationUser> findByUsername(String username);

}
