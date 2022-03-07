import { Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import {
  ALL_TWEETS_URL,
  COMMENTS_BY_TWEET_URL,
  COMMENT_LIKE_URL,
  DELETE_COMMENT_URL,
  DELETE_TWEET_URL,
  POST_COMMENT_URL,
  POST_TWEET_URL,
  TWEET_LIKE_URL,
  UPDATE_COMMENT_URL,
  UPDATE_TWEET_URL,
  USER_TWEET_URL
} from 'src/app/models/api.constants';
import { MessageOnlyResponse } from 'src/app/models/general.model';
import {
  Comment,
  CommentLikeToggleRequest,
  CommentRequest,
  CommentResponse,
  DeleteComment,
  PostComment,
  PostCommentResponse,
  PostTweetRequest,
  Tweet,
  TweetLikeToggleRequest,
  TweetResponse,
  TweetWithComments,
  UpdateComment,
  UpdateTweetRequest,
  UsernameTweetRequest
} from 'src/app/models/tweet.model';
import { HttpService } from 'src/app/services/http.service';

@Injectable({
  providedIn: 'root'
})
export class TweetService {

  constructor(private http: HttpService) { }

  private readonly structuredTweet: TweetResponse = {
    messages: [{
      code: 'APP_E_SVC_UNAVAILABLE',
      message: 'Service Unavailable.',
      type: 'INFO'
    }],
    tweets: [],
    username: ''
  };

  /**
 * Get all tweets in database
 * @returns Observable with tweets
 */
  public fetchAllTweets(): Observable<TweetResponse> {
    return this.http.get<TweetResponse>(ALL_TWEETS_URL);
  }

  /**
* Get all tweets and its respective comments
* @returns Observable with Tweets and Comments for the tweets
*/
  public fetchStructuredTweets(): Observable<TweetWithComments[]> {
    return this.fetchAllTweets().pipe(
      catchError(() => {
        return of(this.structuredTweet);
      }),
      map(tweetList => {
        const structuredTweet: TweetWithComments[] = [];
        tweetList.tweets.forEach((tweet: Tweet) => {
          const comments: Comment[] = [];
          structuredTweet.push({ tweet, comments });
        });
        return structuredTweet;
      }));
  }

  /**
   * Fetch tweets of a particular user
   * @param username username
   * @returns Observable<TweetWithComments[]> from server
   */
  public fetchTweetsAsUser(username: string): Observable<TweetWithComments[]> {
    return this.http.post<UsernameTweetRequest, TweetResponse>(USER_TWEET_URL, { username }).pipe(
      catchError(() => {
        return of(this.structuredTweet);
      }),
      map(tweetList => {
        const filteredTweets: TweetWithComments[] = [];
        tweetList.tweets.forEach(tweet => {
          filteredTweets.push({ tweet, comments: [] });
        });
        return filteredTweets;
      }));
  }

  /**
   * Post a new Tweet
   * @param tweet Tweet to be posted
   * @returns Observable<MessageOnlyResponse> from server
   */
  public postNewTweet(tweet: Tweet): Observable<MessageOnlyResponse> {
    return this.http.post<PostTweetRequest, MessageOnlyResponse>(POST_TWEET_URL, { username: tweet.username, message: tweet.message });
  }

  /**
   * Update an existing Tweet
   * @param tweet Tweet to be updated
   * @returns Observable<MessageOnlyResponse> from server
   */
  public updateTweet(tweet: UpdateTweetRequest): Observable<MessageOnlyResponse> {
    return this.http.put<UpdateTweetRequest, MessageOnlyResponse>(UPDATE_TWEET_URL, tweet);
  }

  /**
   * Delete a tweet
   * @param tweet Tweet to be deleted
   * @returns Observable<MessageOnlyResponse> from server
   */
  public deleteTweet(tweet: Tweet): Observable<MessageOnlyResponse> {
    return this.http.delete<Tweet, MessageOnlyResponse>(DELETE_TWEET_URL, tweet);
  }

  /**
   * Toggle likes for a tweet by a user
   * @param data data with tweetId and username
   * @returns Observable<MessageOnlyResponse> from server
   */
  public toggleTweetLike(data: TweetLikeToggleRequest): Observable<MessageOnlyResponse> {
    return this.http.put<TweetLikeToggleRequest, MessageOnlyResponse>(TWEET_LIKE_URL, data);
  }

  /**
   * Get comments for a particular tweet
   * @param tweetId tweetId to fetch comment for
   * @returns Observable with Comments
   */
  public fetchTweetComments(tweetId: string): Observable<CommentResponse> {
    return this.http.post<CommentRequest, CommentResponse>(COMMENTS_BY_TWEET_URL, { tweetId });
  }

  /**
   * Post a new Comment on a Tweet
   * @param comment comment to be posted
   * @param tweetId tweetId of the tweet commented on
   * @param username username of the user who commented
   * @returns Observable<CommentResponse> from the server
   */
  public postTweetComments(comment: Comment, tweetId: string, username: string): Observable<PostCommentResponse> {
    return this.http.post<PostComment, PostCommentResponse>(POST_COMMENT_URL, { message: comment.message, tweetId, username });
  }

  /**
   * Update Comment on a Tweet
   * @param message message of the updated comment
   * @param commentId comment id of the updated comment
   * @param username username of the user updating the comment
   * @returns Observable<MessageOnlyResponse> from server
   */
  public updateTweetComments(message: string, commentId: string, username: string): Observable<MessageOnlyResponse> {
    return this.http.put<UpdateComment, MessageOnlyResponse>(UPDATE_COMMENT_URL, { message, commentId, username });
  }

  /**
   * Delete a Commment on the Tweet
   * @param commentId comment id to be deleted
   * @param username username of the user
   * @returns Observable<MessageOnlyResponse> from server
   */
  public deleteTweetComments(commentId: string, username: string): Observable<MessageOnlyResponse> {
    return this.http.delete<DeleteComment, MessageOnlyResponse>(DELETE_COMMENT_URL, { commentId, username });
  }

  /**
   * Toggle Comment likes
   * @param data comment id and username
   * @returns Observable<MessageOnlyResponse> from server
   */
  public toggleCommentLike(data: CommentLikeToggleRequest): Observable<MessageOnlyResponse> {
    return this.http.put<CommentLikeToggleRequest, MessageOnlyResponse>(COMMENT_LIKE_URL, data);
  }

  /**
   * Process date for display string
   * @param date Date to be processed
   * @returns string with the processed date
   */
  public processTimeStamp(date: string): string {
    const dt = new Date(date);
    let formattedTimeStamp = '';
    const dateNow = new Date();
    const dateDiff = (Math.floor(dateNow.getTime() - dt.getTime())) / (1000 * 3600 * 24);
    if (dateDiff > 364)
      formattedTimeStamp = Math.floor(dateDiff / 365) + ' years ago';
    else if (dateDiff > 30)
      formattedTimeStamp = Math.floor(dateDiff / 30) + ' months ago';
    else if (dateDiff > 7)
      formattedTimeStamp = Math.floor(dateDiff / 7) + ' weeks ago';
    else if (dateDiff > 1)
      formattedTimeStamp = Math.floor(dateDiff) + ' days ago';
    else if (Math.floor(dateDiff * 24) > 0)
      formattedTimeStamp = Math.floor(dateDiff * 24) + ' hours ago';
    else if (Math.floor(dateDiff * 24 * 60) > 0)
      formattedTimeStamp = Math.floor(dateDiff * 24 * 60) + ' mins ago';
    else
      formattedTimeStamp = 'few moments ago';
    return formattedTimeStamp;
  }
}
