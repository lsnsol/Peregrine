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

import com.peregrine.tweet.bean.CommentBean;
import com.peregrine.tweet.bean.TweetBean;

@Component
public class CommentRepositoryImpl implements CommentRepository {

	@Autowired
	@Qualifier("mongoTemplate")
	MongoTemplate mongoTemplate;
	
	public void save(CommentBean commentBean) {
		mongoTemplate.save(commentBean);
	}
	
	public void updateCommentMessage(CommentBean commentBean) {
		CommentBean commentBeanFromDb = mongoTemplate.findOne(
				 Query.query(Criteria.where("commentId").is(commentBean.getCommentId())), CommentBean.class);
		
		if(Objects.nonNull(commentBeanFromDb)) {
			commentBeanFromDb.setMessage(commentBean.getMessage());
			commentBeanFromDb.setUpdateDttm(new Date());
			mongoTemplate.save(commentBeanFromDb);
		}
		
	}
	
	public void updateCommentLike(CommentBean commentBean, String username) {
		List<String> likesUsernames = null;
		CommentBean commentBeanFromDb = mongoTemplate.findOne(
				 Query.query(Criteria.where("commentId").is(commentBean.getCommentId())), CommentBean.class);
		commentBeanFromDb.setUpdateDttm(new Date());
		if(Objects.nonNull(commentBeanFromDb)) {
			//processLikes
			if(CollectionUtils.isEmpty(commentBeanFromDb.getLikesUsernames())) {
				likesUsernames = new ArrayList<>();
				likesUsernames.add(username);
				commentBeanFromDb.setLikesUsernames(likesUsernames);
				commentBeanFromDb.setLikes(1);
			} else if(commentBeanFromDb.getLikesUsernames().contains(username)) {
				likesUsernames = new ArrayList<>();
				likesUsernames.addAll(commentBeanFromDb.getLikesUsernames());
				likesUsernames.remove(username);
				commentBeanFromDb.setLikesUsernames(likesUsernames);
				commentBeanFromDb.setLikes(commentBeanFromDb.getLikes() - 1);
			} else if(!commentBeanFromDb.getLikesUsernames().contains(username)) {
				likesUsernames = new ArrayList<>();
				likesUsernames.addAll(commentBeanFromDb.getLikesUsernames());
				likesUsernames.add(username);
				commentBeanFromDb.setLikesUsernames(likesUsernames);
				commentBeanFromDb.setLikes(commentBeanFromDb.getLikes() + 1);
			
			}
			if(commentBeanFromDb.getLikes()<0) {
				commentBeanFromDb.setLikes(0);
			}
			mongoTemplate.save(commentBeanFromDb);
		}
			
	}
	@Override
	public List<CommentBean> getAllCommentsByTweetId(String tweetId) {
		
		List<CommentBean> commentBeansFromDb = mongoTemplate.find(Query.query(Criteria.where("tweetId").is(tweetId)),
				CommentBean.class);
		return commentBeansFromDb;
	}
	
	public Long getCommentsCountByTweetId(String tweetId) {
		
		return mongoTemplate.count(Query.query(Criteria.where("tweetId").is(tweetId)),
				CommentBean.class);
	}
	
	public CommentBean deleteComment(CommentBean commentBean) {
		CommentBean commentBeanFromDb = mongoTemplate.findOne(
				 Query.query(Criteria.where("commentId").is(commentBean.getCommentId())), CommentBean.class);
		if(Objects.nonNull(commentBeanFromDb)) {
			mongoTemplate.remove(commentBeanFromDb);
		}
		return commentBeanFromDb;
	}
	
	public void deleteCommentsByTweetId(String tweetId) {
		mongoTemplate.findAllAndRemove(Query.query(Criteria.where("tweetId").is(tweetId)), CommentBean.class);
	}

}
