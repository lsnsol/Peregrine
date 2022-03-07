package com.peregrine.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peregrine.register.bean.PeregrineUser;
import com.peregrine.register.bean.RegistrationRequest;
import com.peregrine.repository.ProfilesRepository;
import com.peregrine.utils.LoggingUtils;

@Service
public class ProfileRegistrationManager {
	
	@Autowired
	ProfilesRepository profilesRepository;

	public void processUserCreation(RegistrationRequest reqBean) {
		try {
			PeregrineUser newUser = new PeregrineUser();
			newUser.setFirstName(reqBean.getFirstName());
			newUser.setLastName(reqBean.getLastName());
			newUser.setEmail(reqBean.getEmail());
			newUser.setPassword(reqBean.getPassword());
			newUser.setContactNumber(reqBean.getContactNumber());
			newUser.setCreateDttm(new Date());
			newUser.setUpdateDttm(new Date());
			newUser.setUsername(reqBean.getUsername());
			newUser.setUsername(reqBean.getUsername());
			newUser.setAvatar(reqBean.getAvatar());
			profilesRepository.save(newUser);
		} catch(Exception e) {
			LoggingUtils.logError("PERSIST_FATAL_ERROR - Error in persisting user into database", e);
		}
	}
	
	public List<PeregrineUser> findUserByUsername(String username) {
		List<PeregrineUser> profiles = profilesRepository.findByUsername(username);
		return profiles;
	}
	
	public List<PeregrineUser> findUserByEmail(String email) {
		List<PeregrineUser> profiles = profilesRepository.findByEmail(email);
		return profiles;
	}
	
}
