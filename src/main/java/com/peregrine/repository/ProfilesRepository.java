package com.peregrine.repository;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.peregrine.register.bean.PeregrineUser;

@Repository
public interface ProfilesRepository {

	public List<PeregrineUser> findByUsername(String username);
	
	public List<PeregrineUser> findByUsernamesList(Set<String> usernameList);
	
	public List<PeregrineUser> findByEmail(String email);
	
	public void save(PeregrineUser user);
	
	public void saveNewPassword(String username, String password);

}
