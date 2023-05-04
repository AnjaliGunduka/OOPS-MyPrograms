package com.online.web.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.online.web.model.PersistentLogin;
@org.springframework.stereotype.Repository
public interface RememberMeTokenRepository extends CrudRepository<PersistentLogin, Long> {

	PersistentLogin findBySeries(String series);
	List<PersistentLogin> findByUsername(String username);
	void delete(List<PersistentLogin> persistentLogins);

}
