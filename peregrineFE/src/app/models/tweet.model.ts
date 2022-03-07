import { MessageResponse } from "./general.model";
import { Avatars } from "./profile.model";

export interface Tweet {
  tweetId: string;
  message: string;
  username: string;
  likes: number;
  likeUserNames: string[];
  noOfComments: number;
  avatar: Avatars;
  createDttm?: string;
  updateDttm?: string;
}

export interface Comment {
  tweetId: string;
  commentId: string;
  username: string;
  message: string;
  likes: number;
  likeUserNames: string[];
  edit?: boolean;
  createDttm?: string;
  updateDttm?: string;
}

export interface TweetWithComments {
  tweet: Tweet;
  comments: Comment[];
}

export interface UsernameTweetRequest {
  username: string;
}

export interface TweetResponse {
  username: string;
  messages: MessageResponse[];
  tweets: Tweet[];
}

export interface PostTweetRequest {
  username: string;
  message: string;
}

export interface UpdateTweetRequest {
  tweetId: string;
  message: string;
  username: string;
}

export interface TweetLikeToggleRequest {
  tweetId: string;
  username: string;
}

export interface CommentLikeToggleRequest {
  commentId: string;
  username: string;
}

export interface PostComment {
  message: string;
  username: string;
  tweetId: string;
}

export interface PostCommentResponse {
  messages: MessageResponse[];
  commentId: string;
}

export interface UpdateComment {
  message: string;
  username: string;
  commentId: string;
}

export interface DeleteComment {
  commentId: string;
  username: string;
}

export interface CommentResponse {
  username: string;
  messages: MessageResponse[];
  comments: Comment[];
}

export interface CommentRequest {
  tweetId: string;
}