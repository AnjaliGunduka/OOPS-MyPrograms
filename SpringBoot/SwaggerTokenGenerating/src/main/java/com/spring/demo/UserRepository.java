package com.spring.demo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



/**
 * 
 * 
 * @author Anjali
 *
 */
@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Long> {

	Optional<ApplicationUser> findByUsername(String username);

}
