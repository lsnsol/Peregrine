import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CMT_I_1001, CMT_I_1003, CMT_I_1005, CMT_I_1007, TWT_I_1001, TWT_I_1005, TWT_I_1007 } from 'src/app/models/error.constants';
import { MessageOnlyResponse } from 'src/app/models/general.model';
import { Avatars } from 'src/app/models/profile.model';
import { Comment, CommentResponse, PostCommentResponse, Tweet, TweetWithComments } from 'src/app/models/tweet.model';
import { DataService } from 'src/app/services/data.service';
import { AlertService } from '../alert/alert.service';
import { LoaderService } from '../loader/loader.service';
import { TweetService } from './tweet.service';

@Component({
  selector: 'peregrine-tweet',
  templateUrl: './tweet.component.html',
  styleUrls: ['./tweet.component.css']
})
export class TweetComponent implements OnInit {
  @Input() avatar: Avatars = 'default';
  @Input() username: string | undefined = '';
  @Input() fullname: string | undefined = '';
  @Input() addNewTweetFlag: boolean = false;
  @Input() userPageTweet: boolean = false;
  @Input() otherUser: boolean = false;
  @Input() otherUsername: string = '';
  @Input() showUsernameHighlight: boolean = true;

  public tweets: TweetWithComments[] = [];
  public editTweetFlag: boolean[] = [];
  public tweetDateArray: string[] = [];
  public commentDateArray: { [tweetId: string]: string[] } = {};
  public favoriteIcon: string[] = [];
  public commentFavoriteIcon: { [tweetId: string]: string[] } = {};
  public commentFlag: boolean[] = [];
  public editCommentFlag: boolean[] = [];
  public newComment: string[] = [];
  public newTweet: string = '';

  @Output() public userNameClickEvent: EventEmitter<{ username: string, avatar: string }> = new EventEmitter();

  constructor(
    private tweetService: TweetService,
    private loaderService: LoaderService,
    private alertService: AlertService
  ) { }

  /**
   * Create necessary variable arrays to use in front end
   * @param tweetWithComments the fetched tweets from server
   */
  private createArrays(tweetWithComments: TweetWithComments[]): void {
    tweetWithComments.reverse();
    tweetWithComments.forEach(tweet => {
      tweet.tweet.noOfComments = tweet.tweet.noOfComments ? tweet.tweet.noOfComments : 0;
      tweet.comments.reverse();
      this.editTweetFlag.push(false);
      this.commentFlag.push(false);
      this.editCommentFlag.push(false);
      this.tweetDateArray.push(this.tweetService.processTimeStamp(tweet.tweet.createDttm ? tweet.tweet.createDttm : new Date().toString()));
      if (!tweet.tweet.avatar)
        tweet.tweet.avatar = 'default';
      if (tweet.tweet.likeUserNames.indexOf(this.username + '') != -1)
        this.favoriteIcon.push('favorite');
      else
        this.favoriteIcon.push('favorite_border');
    });
  }

  ngOnInit() {
    this.loaderService.loader = true;
    const tweetObserver = {
      observer: this,
      next(tweets: TweetWithComments[]) {
        if (tweets.length)
          this.observer.alertService.alertMessage('Tweets Fetched Successfully', 'green', 1000);
        else
          this.observer.alertService.alertMessage('No tweets to show', 'blue');
        this.observer.tweets = tweets;
        this.observer.createArrays(tweets);
      },
      error() {
        this.observer.alertService.alertMessage('Server error, cannot fetch user tweets');
      }
    };
    if (!this.addNewTweetFlag)
      if (this.userPageTweet)
        this.tweetService.fetchTweetsAsUser(this.username + '').subscribe(tweetObserver);
      else if (this.otherUser)
        this.tweetService.fetchTweetsAsUser(this.otherUsername).subscribe(tweetObserver);
      else
        this.tweetService.fetchStructuredTweets().subscribe(tweetObserver);
  }

  /**
   * add new tweet
   */
  public addNewTweet() {
    if (this.fullname && this.username && this.newTweet != '' && this.avatar) {
      this.loaderService.loader = true;
      const tweet: Tweet = {
        likes: 0,
        noOfComments: 0,
        likeUserNames: [],
        message: this.newTweet,
        tweetId: Math.random() * 10 + '',
        username: this.username,
        avatar: this.avatar
      }

      const newTweetObserver = {
        observer: this,
        next(messageOnlyResponse: MessageOnlyResponse) {
          if (messageOnlyResponse.messages[0].message === TWT_I_1001) {
            this.observer.newTweet = '';
            this.observer.alertService.alertMessage('Tweets Added Successfully', 'green', 1000);
            location.reload();
          }
          else
            this.observer.alertService.alertMessage('Tweet Add failed', 'yellow');
        },
        error() {
          this.observer.alertService.alertMessage('Server error, cannot add tweet');
        }
      }
      this.tweetService.postNewTweet(tweet).subscribe(newTweetObserver)
    }
  }

  /**
   * Edit button clicked for tweet
   * @param index index of the tweet
   */
  public editTweetButton(index: number) {
    this.editTweetFlag[index] = true;
    setTimeout(() => {
      document.getElementById('edit-tweet-' + this.tweets[index].tweet.tweetId)?.focus();
    }, 100);
  }

  /**
   * confirm button clicked after editing tweet
   * @param index index of the tweet
   */
  public updateTweet(index: number) {
    if (this.tweets[index].tweet.message != '') {
      this.loaderService.loader = true;
      const updateTweetObserver = {
        observer: this,
        next(messageOnlyResponse: MessageOnlyResponse) {
          if (messageOnlyResponse.messages[0].message === TWT_I_1005)
            this.observer.alertService.alertMessage('Tweets Updated Successfully', 'green', 1000);
          else
            this.observer.alertService.alertMessage('Tweet Update failed', 'yellow');
        },
        error() {
          this.observer.alertService.alertMessage('Server error, cannot update tweet');
        }
      }
      this.tweetService.updateTweet({
        username: this.username + '',
        message: this.tweets[index].tweet.message,
        tweetId: this.tweets[index].tweet.tweetId
      }).subscribe(updateTweetObserver);
    }
    this.editTweetFlag[index] = false;
  }

  /**
   * Edit mode cancelled for tweet
   * @param index index of the tweet
   */
  public cancelEditTweet(index: number) {
    this.editTweetFlag[index] = false;
  }

  /**
   * Delete tweet button clicked
   * @param index index of the tweet
   */
  public deleteTweet(index: number) {
    this.loaderService.loader = true;
    const deleteTweetObserver = {
      observer: this,
      next(response: MessageOnlyResponse) {
        if (response.messages[0].message === TWT_I_1007) {
          this.observer.alertService.alertMessage('Tweets Deleted Successfully', 'green', 1000);
          this.observer.tweets.splice(index, 1);
          this.observer.favoriteIcon.splice(index, 1);
          this.observer.editTweetFlag.splice(index, 1);
        }
        else
          this.observer.alertService.alertMessage(response.messages[0].message);
      },
      error() {
        this.observer.alertService.alertMessage('Server error, cannot delete tweets');
      }
    };
    this.tweetService.deleteTweet(this.tweets[index].tweet).subscribe(deleteTweetObserver);
  }

  /**
   * Toggle tweet like
   * @param index index of the tweet
   */
  public toggleTweetLike(index: number) {
    const likeTweetObserver = {
      observer: this,
      next(response: MessageOnlyResponse) {
        if (response.messages[0].message === TWT_I_1005) {
          if (this.observer.favoriteIcon[index] == 'favorite_border') {
            this.observer.favoriteIcon[index] = 'favorite';
            this.observer.tweets[index].tweet.likeUserNames.push(this.observer.username + '');
          }
          else if (this.observer.favoriteIcon[index] == 'favorite') {
            this.observer.favoriteIcon[index] = 'favorite_border';
            const userLiked = this.observer.tweets[index].tweet.likeUserNames.indexOf(this.observer.username + '')
            if (userLiked > -1) {
              this.observer.tweets[index].tweet.likeUserNames.splice(userLiked, 1);
            }
          }
          this.observer.tweets[index].tweet.likes = this.observer.tweets[index].tweet.likeUserNames.length;
        }
        else
          this.observer.alertService.alertMessage(response.messages[0].message);
      },
      error() {
        this.observer.alertService.alertMessage();
      }
    };
    this.tweetService.toggleTweetLike({
      tweetId: this.tweets[index].tweet.tweetId,
      username: this.username + ''
    }).subscribe(likeTweetObserver)
  }

  /**
   * Show hide tweet comments
   * @param index index of the tweet
   */
  public toggleComments(index: number) {
    if (!this.commentFlag[index]) {
      this.loaderService.loader = true;
      const fetchCommentsObserver = {
        observer: this,
        next(comments: CommentResponse) {
          if (comments.messages[0].message != CMT_I_1003) {
            this.observer.alertService.alertMessage('Comments fetch failed, try again');
            this.observer.commentFlag[index] = false;
          }
          this.observer.commentFlag[index] = !this.observer.commentFlag[index];
          this.observer.tweets[index].comments = comments.comments;
          const commentLikes: string[] = [];
          const commentDateArr: string[] = [];
          comments.comments.forEach(comment => {
            commentDateArr.push(this.observer.tweetService.processTimeStamp(comment.createDttm ? comment.createDttm : new Date().toString()));
            if (comment.likeUserNames.indexOf(this.observer.username + '') != -1)
              commentLikes.push('favorite');
            else
              commentLikes.push('favorite_border');
          });
          this.observer.commentFavoriteIcon[this.observer.tweets[index].tweet.tweetId] = commentLikes;
          this.observer.commentDateArray[this.observer.tweets[index].tweet.tweetId] = commentDateArr;
          this.observer.loaderService.loader = false;
          document.getElementById('add-comment-' + this.observer.tweets?.[index].tweet.tweetId)?.focus();
        },
        error() {
          this.observer.alertService.alertMessage('Server error, fetch add comment');
        }
      };
      this.tweetService.fetchTweetComments(this.tweets[index].tweet.tweetId).subscribe(fetchCommentsObserver);
    } else {
      this.commentFlag[index] = !this.commentFlag[index];
    }
  }

  /**
   * Add new comment to a tweet
   * @param index index of the tweet
   */
  public addComment(index: number) {
    if (this.username && this.newComment[index] != '') {
      this.loaderService.loader = true;
      const comment: Comment = {
        message: this.newComment[index],
        commentId: '',
        tweetId: this.tweets[index].tweet.tweetId,
        username: this.username,
        likeUserNames: [],
        likes: 0
      };
      this.commentFavoriteIcon[this.tweets[index].tweet.tweetId].push('favorite_border');
      this.commentDateArray[this.tweets[index].tweet.tweetId].push(this.tweetService.processTimeStamp(new Date().toString()));
      const commentAddObserver = {
        observer: this,
        next(response: PostCommentResponse) {
          if (response.messages[0].message === CMT_I_1001) {
            this.observer.alertService.alertMessage('Comment Added Successfully', 'green', 1000);
            comment.commentId = response.commentId;
            this.observer.tweets?.[index].comments.push(comment);
            this.observer.tweets[index].tweet.noOfComments += 1;
          }
          else
            this.observer.alertService.alertMessage(response.messages[0].message);
          this.observer.clearCommentInput(index);
        },
        error() {
          this.observer.alertService.alertMessage('Server error, cannot add comment');
        }
      };
      this.tweetService.postTweetComments(comment, this.tweets[index].tweet.tweetId, this.username).subscribe(commentAddObserver);
    }
  }

  /**
   * Comment input cleared
   */
  public clearCommentInput(index: number) {
    this.newComment[index] = '';
  }

  /**
   * Edit comment button clicked
   * @param tweetIndex index of the tweet
   * @param commentIndex index of the comment
   * @param comment edited comment
   */
  public editComment(tweetIndex: number, commentIndex: number, comment: Comment) {
    comment.edit = true;
    setTimeout(() => {
      document.getElementById('edit-comment-' + comment.commentId)?.focus();
    }, 100);
    this.tweets[tweetIndex].comments[commentIndex].edit = true;
  }

  /**
   * Comment update button clicked
   * @param tweetIndex index of the tweet
   * @param commentIndex index of the comment
   */
  public editCommentConfirm(tweetIndex: number, commentIndex: number) {
    const updateCommentObserver = {
      observer: this,
      next(messageOnlyResponse: MessageOnlyResponse) {
        if (messageOnlyResponse.messages[0].message === CMT_I_1005) {
          this.observer.alertService.alertMessage('Comment Updated Successfully', 'green', 1000);
          document.getElementById('add-comment-' + this.observer.tweets?.[tweetIndex].tweet.tweetId)?.focus();
        }
        else
          this.observer.alertService.alertMessage('Comment Update failed', 'yellow');
      },
      error() {
        this.observer.alertService.alertMessage('Server error, cannot update comment');
      }
    }
    this.tweetService.updateTweetComments(
      this.tweets[tweetIndex].comments[commentIndex].message,
      this.tweets[tweetIndex].comments[commentIndex].commentId,
      this.username + ''
    ).subscribe(updateCommentObserver);
  }

  /**
   * Delete comment button clicked
   * @param tweetIndex index of the tweet
   * @param commentIndex index of the comment
   */
  public deleteComment(tweetIndex: number, commentIndex: number) {
    const deleteCommentObserver = {
      observer: this,
      next(response: MessageOnlyResponse) {
        if (response.messages[0].message === CMT_I_1007) {
          this.observer.alertService.alertMessage('Comment Deleted Successfully', 'green', 1000);
          this.observer.tweets[tweetIndex].comments.splice(commentIndex, 1);
          this.observer.editCommentFlag.splice(commentIndex, 1);
          delete this.observer.commentFavoriteIcon[this.observer.tweets[tweetIndex].tweet.tweetId];
          this.observer.commentFlag.splice(commentIndex, 1);
          this.observer.tweets[tweetIndex].tweet.noOfComments -= 1;
        }
        else
          this.observer.alertService.alertMessage(response.messages[0].message);
      },
      error() {
        this.observer.alertService.alertMessage('Server error, cannot delete comment');
      }
    };
    this.tweetService.deleteTweetComments(this.tweets[tweetIndex].comments[commentIndex].commentId, this.username + '').subscribe(deleteCommentObserver);
  }

  /**
   * Toggle like on comment
   * @param tweetIndex index of the tweet
   * @param commentIndex index of the comment
   */
  public toggleCommentLike(tweetIndex: number, commentIndex: number) {
    const likeCommentObserver = {
      observer: this,
      next(response: MessageOnlyResponse) {
        const tweetId = this.observer.tweets[tweetIndex].tweet.tweetId;
        const comment = this.observer.tweets[tweetIndex].comments[commentIndex];
        if (response.messages[0].message === CMT_I_1005) {
          if (this.observer.commentFavoriteIcon[tweetId][commentIndex] == 'favorite_border') {
            this.observer.commentFavoriteIcon[tweetId][commentIndex] = 'favorite';
            comment.likeUserNames.push(this.observer.username + '');
          }
          else if (this.observer.commentFavoriteIcon[tweetId][commentIndex] == 'favorite') {
            this.observer.commentFavoriteIcon[tweetId][commentIndex] = 'favorite_border';
            const userLiked = comment.likeUserNames.indexOf(this.observer.username + '')
            if (userLiked > -1) {
              comment.likeUserNames.splice(userLiked, 1);
            }
          }
          comment.likes = comment.likeUserNames.length;
        }
        else
          this.observer.alertService.alertMessage(response.messages[0].message);
      },
      error() {
        this.observer.alertService.alertMessage();
      }
    };
    this.tweetService.toggleCommentLike({
      commentId: this.tweets[tweetIndex].comments[commentIndex].commentId,
      username: this.username + ''
    }).subscribe(likeCommentObserver)
  }

  /**
   * Handle keyboard events Enter and Escape
   * @param index index of the tweet
   * @param event keyboard event emitted
   * @param source source of the event based on UI
   * @param commentIndex index of the comment 
   */
  public handleKeyboardEvent(index: number, event: KeyboardEvent, source: 'Tweet' | 'Comment' | 'EditTweet' | 'EditComment', commentIndex?: number) {
    if (event.key === 'Enter') {
      switch (source) {
        case 'Tweet': this.addNewTweet(); break;
        case 'EditTweet': this.updateTweet(index); break;
        case 'Comment': this.addComment(index); break;
        case 'EditComment':
          if (typeof (commentIndex) === 'number') {
            this.editCommentConfirm(index, commentIndex);
          }
          break;
      }
    } else if (event.key === 'Escape') {
      switch (source) {
        case 'Tweet':
          this.newTweet = '';
          document.getElementById('add-comment')?.blur();
          break;
        case 'Comment':
          this.newComment[index] = '';
          document.getElementById('add-comment')?.blur();
          break;
        case 'EditComment':
          document.getElementById('edit-comment')?.blur();
          break;
      }
    }
  }

  public userNameClicked(username: string, avatar: string) {
    this.userNameClickEvent.emit({ username, avatar });
  }
}
