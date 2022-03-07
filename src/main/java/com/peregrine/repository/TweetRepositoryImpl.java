package com.peregrine.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.peregrine.tweet.bean.TweetBean;

@Component
public class TweetRepositoryImpl implements TweetRepository {

	@Autowired
	@Qualifier("mongoTemplate")
	MongoTemplate mongoTemplate;
	
	public void save(TweetBean tweetBean) {
		mongoTemplate.save(tweetBean);
	}
	
	public void updateTweetMessage(TweetBean tweetBean) {
		TweetBean tweetBeanFromDb = mongoTemplate.findOne(
				 Query.query(Criteria.where("tweetId").is(tweetBean.getTweetId())), TweetBean.class);
		if(Objects.nonNull(tweetBeanFromDb)) {
			tweetBeanFromDb.setMessage(tweetBean.getMessage());
			tweetBeanFromDb.setUpdateDttm(new Date());
			mongoTemplate.save(tweetBeanFromDb);
		}
	}
	
	public void updateTweetLike(TweetBean tweetBean, String username) {
		List<String> likesUsernames = null;
		TweetBean tweetBeanFromDb = mongoTemplate.findOne(
				 Query.query(Criteria.where("tweetId").is(tweetBean.getTweetId())), TweetBean.class);
		if(Objects.nonNull(tweetBeanFromDb)) {
			tweetBeanFromDb.setUpdateDttm(new Date());
			//processLikes
			if(CollectionUtils.isEmpty(tweetBeanFromDb.getLikesUsernames())) {
				likesUsernames = new ArrayList<>();
				likesUsernames.add(username);
				tweetBeanFromDb.setLikesUsernames(likesUsernames);
				tweetBeanFromDb.setLikes(1);
			} else if(tweetBeanFromDb.getLikesUsernames().contains(username)) {
				likesUsernames = new ArrayList<>();
				likesUsernames.addAll(tweetBeanFromDb.getLikesUsernames());
				likesUsernames.remove(username);
				tweetBeanFromDb.setLikesUsernames(likesUsernames);
				tweetBeanFromDb.setLikes(tweetBeanFromDb.getLikes() - 1);
			} else if(!tweetBeanFromDb.getLikesUsernames().contains(username)) {
				likesUsernames = new ArrayList<>();
				likesUsernames.addAll(tweetBeanFromDb.getLikesUsernames());
				likesUsernames.add(username);
				tweetBeanFromDb.setLikesUsernames(likesUsernames);
				tweetBeanFromDb.setLikes(tweetBeanFromDb.getLikes() + 1);
			
			}
			if(tweetBeanFromDb.getLikes()<0) {
				tweetBeanFromDb.setLikes(0);
			}
			mongoTemplate.save(tweetBeanFromDb);
		}
		
		
	}
	
	@Override
	public List<TweetBean> getAllTweetsByUsername(String username) {
		
		List<TweetBean> tweetBeansFromDb = mongoTemplate.find(Query.query(Criteria.where("username").is(username)),
				TweetBean.class);
		return tweetBeansFromDb;
	}
	
	@Override
	public List<TweetBean> getAllTweets() {
		
		List<TweetBean> tweetBeansFromDb = mongoTemplate.findAll(TweetBean.class);
		return tweetBeansFromDb;
	}
	
	public void deleteTweet(TweetBean tweetBean) {
		TweetBean tweetBeanFromDb = mongoTemplate.findOne(
				 Query.query(Criteria.where("tweetId").is(tweetBean.getTweetId())), TweetBean.class);
		if(Objects.nonNull(tweetBeanFromDb)) {
			mongoTemplate.remove(tweetBeanFromDb);
		}
		
	}

	@Override
	public TweetBean getTweetById(String tweetId) {
		TweetBean tweetBeanFromDb = mongoTemplate.findOne(
				Query.query(Criteria.where("tweetId").is(tweetId)),
				TweetBean.class);
		return tweetBeanFromDb;
	}
	
	public void incrementCommentCount(String tweetId) {
		TweetBean tweetBeanFromDb = mongoTemplate.findOne(
				 Query.query(Criteria.where("tweetId").is(tweetId)), TweetBean.class);
		if(Objects.nonNull(tweetBeanFromDb)) {
			Long tweetCountFromDB = Objects.nonNull(tweetBeanFromDb) && Objects.nonNull(tweetBeanFromDb.getNoOfComments())? tweetBeanFromDb.getNoOfComments() : 0;
			tweetBeanFromDb.setNoOfComments(tweetCountFromDB + 1);
			mongoTemplate.save(tweetBeanFromDb);
		}
		
	}
	
	public void decrementCommentCount(String tweetId) {
		TweetBean tweetBeanFromDb = mongoTemplate.findOne(
				 Query.query(Criteria.where("tweetId").is(tweetId)), TweetBean.class);
		if(Objects.nonNull(tweetBeanFromDb)) {
			Long tweetCountFromDB = Objects.nonNull(tweetBeanFromDb) && Objects.nonNull(tweetBeanFromDb.getNoOfComments())? tweetBeanFromDb.getNoOfComments() : 0;
			tweetBeanFromDb.setNoOfComments(tweetCountFromDB- 1);
			mongoTemplate.save(tweetBeanFromDb);
		}
		
	}

}
