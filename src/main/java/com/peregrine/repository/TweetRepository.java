package com.peregrine.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.peregrine.tweet.bean.TweetBean;

@Repository
public interface TweetRepository {

	public void save(TweetBean tweetBean);
	
	public void updateTweetMessage(TweetBean tweetBean);
	
	public void updateTweetLike(TweetBean tweetBean, String username);
	
	public List<TweetBean> getAllTweetsByUsername(String username);
	
	public TweetBean getTweetById(String tweetId);
	
	public List<TweetBean> getAllTweets();
	
	public void deleteTweet(TweetBean tweetBean);
	
	public void incrementCommentCount(String tweetId);
	
	public void decrementCommentCount(String tweetId);
}
