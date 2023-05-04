package com.online.web.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.web.model.Users;

@Repository

public interface UsersDao extends CrudRepository<Users, Long>{

	Users findUserByusername(String username);
}
