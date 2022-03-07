package com.peregrine.repository;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.peregrine.register.bean.PeregrineUser;

@Component
public class ProfilesRepositoryImpl implements ProfilesRepository {

	@Autowired
	@Qualifier("mongoTemplate")
	MongoTemplate mongoTemplate;
	
	@Override
	public List<PeregrineUser> findByUsername(String username) {
		List<PeregrineUser> profiles = null;
		Query query = Query.query(Criteria.where("username").is(username));
		profiles = mongoTemplate.find(query, PeregrineUser.class);
		return profiles;
	}
	
	public List<PeregrineUser> findByUsernamesList(Set<String> usernamesList) {
		List<PeregrineUser> profiles = null;
		Query query = Query.query(Criteria.where("username").in(usernamesList));
		profiles = mongoTemplate.find(query, PeregrineUser.class);
		return profiles;
	}

	public List<PeregrineUser> findByEmail(String email) {
		List<PeregrineUser> profiles = null;
		Query query = Query.query(Criteria.where("email").is(email));
		profiles = mongoTemplate.find(query, PeregrineUser.class);
		return profiles;
	}
	
	@Override
	public void save(PeregrineUser user) {
		mongoTemplate.save(user);
	}
	
	public void saveNewPassword(String username, String password) {
		PeregrineUser profile = null;
		Query query = Query.query(Criteria.where("username").is(username));
		profile = mongoTemplate.findOne(query, PeregrineUser.class);
		profile.setPassword(password);
		mongoTemplate.save(profile);
	}
	
}
