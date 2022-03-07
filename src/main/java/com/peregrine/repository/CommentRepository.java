package com.peregrine.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.peregrine.tweet.bean.CommentBean;

@Repository
public interface CommentRepository {

	public void save(CommentBean commentBean);
	
	public void updateCommentMessage(CommentBean commentBean);
	
	public void updateCommentLike(CommentBean commentBean, String username);
	
	public List<CommentBean> getAllCommentsByTweetId(String tweetId);
	
	public CommentBean deleteComment(CommentBean commentBean);
	
	public void deleteCommentsByTweetId(String tweetId);
	
	public Long getCommentsCountByTweetId(String tweetId);
}
