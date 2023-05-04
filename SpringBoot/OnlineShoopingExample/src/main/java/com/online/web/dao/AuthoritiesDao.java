package com.online.web.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.web.model.Authorities;

@Repository

public interface AuthoritiesDao extends CrudRepository<Authorities, Long>{

	Authorities findAuthoritiesByusername(String username);
}
