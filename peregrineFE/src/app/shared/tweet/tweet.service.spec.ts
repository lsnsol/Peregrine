import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { Comment, Tweet } from 'src/app/models/tweet.model';
import { HttpService } from 'src/app/services/http.service';
import { TweetService } from './tweet.service';


describe('TweetService', () => {
  let service: TweetService;
  let httpService: jasmine.SpyObj<HttpService>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj(
      'HttpService',
      {
        get: of(),
        post: of(),
        put: of(),
        delete: of()
      });
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        TweetService,
        {
          provide: HttpService,
          useValue: spy
        }
      ]
    });
    service = TestBed.inject(TweetService);
    httpService = TestBed.inject(HttpService) as jasmine.SpyObj<HttpService>;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should have fetchAllTweets function and called', () => {
    expect(service.fetchAllTweets).toBeDefined();
    expect(service.fetchAllTweets()).withContext('service to return Observable<TweetResponse>');
    expect(httpService.get.calls.count())
      .withContext('spy method was called once')
      .toBe(1);
  });

  it('should have fetchStructuredTweets function and called which should call fetchAllTweets', () => {
    expect(service.fetchStructuredTweets).toBeDefined();
    spyOn(service, 'fetchAllTweets').and.callThrough();
    expect(service.fetchStructuredTweets()).withContext('service to return Observable<TweetResponse>');
    expect(httpService.get.calls.count())
      .withContext('spy method was called once')
      .toBe(1);
    expect(service.fetchAllTweets).toHaveBeenCalled();
  });

  it('should have fetchTweetsAsUser function and called', () => {
    expect(service.fetchTweetsAsUser).toBeDefined();
    expect(service.fetchTweetsAsUser('JohnDoe1234')).withContext('service to return Observable<TweetResponse>');
    expect(httpService.post.calls.count())
      .withContext('spy method was called once')
      .toBe(1);
  });

  it('should have postNewTweet function and called', () => {
    expect(service.postNewTweet).toBeDefined();
    const tweet: Tweet = {
      avatar: 'man',
      likeUserNames: ['Helen1234', 'Jimmy1234'],
      likes: 2,
      noOfComments:5,
      message: 'test',
      tweetId: 'JohnDoe1234-1af419cf-e313-4798-b944-3a8b464b3080',
      username: 'JohnDoe1234'
    };
    expect(service.postNewTweet(tweet)).withContext('service to return Observable<TweetResponse>');
    expect(httpService.post.calls.count())
      .withContext('spy method was called once')
      .toBe(1);
  });

  it('should have updateTweet function and called', () => {
    expect(service.updateTweet).toBeDefined();
    const tweet: Tweet = {
      avatar: 'man',
      likeUserNames: ['Helen1234', 'Jimmy1234'],
      likes: 2,
      noOfComments:5,
      message: 'test',
      tweetId: 'JohnDoe1234-1af419cf-e313-4798-b944-3a8b464b3080',
      username: 'JohnDoe1234'
    };
    expect(service.updateTweet(tweet)).withContext('service to return Observable<TweetResponse>');
    expect(httpService.put.calls.count())
      .withContext('spy method was called once')
      .toBe(1);
  });

  it('should have deleteTweet function and called', () => {
    expect(service.deleteTweet).toBeDefined();
    const tweet: Tweet = {
      avatar: 'man',
      likeUserNames: ['Helen1234', 'Jimmy1234'],
      likes: 2,
      noOfComments:5,
      message: 'test',
      tweetId: 'JohnDoe1234-1af419cf-e313-4798-b944-3a8b464b3080',
      username: 'JohnDoe1234'
    };
    expect(service.deleteTweet(tweet)).withContext('service to return Observable<TweetResponse>');
    expect(httpService.delete.calls.count())
      .withContext('spy method was called once')
      .toBe(1);
  });

  it('should have toggleTweetLike function and called', () => {
    expect(service.toggleTweetLike).toBeDefined();
    expect(service.toggleTweetLike({
      tweetId: 'JohnDoe1234-1af419cf-e313-4798-b944-3a8b464b3080',
      username: 'JohnDoe1234'
    })).withContext('service to return Observable<TweetResponse>');
    expect(httpService.put.calls.count())
      .withContext('spy method was called once')
      .toBe(1);
  });

  it('should have postTweetComments function and called', () => {
    expect(service.postTweetComments).toBeDefined();
    const comment: Comment = {
      commentId: 'JohnDoe1234-9ebed2aa-c697-4d0a-8b08-991542fb9d4c',
      likeUserNames: ['Helen1234', 'Jimmy1234'],
      likes: 2,
      message: 'test',
      tweetId: 'JohnDoe1234-1af419cf-e313-4798-b944-3a8b464b3080',
      username: 'JohnDoe1234'
    };
    expect(service.postTweetComments(comment, comment.tweetId, comment.username)).withContext('service to return Observable<TweetResponse>');
    expect(httpService.post.calls.count())
      .withContext('spy method was called once')
      .toBe(1);
  });

  it('should have updateTweetComments function and called', () => {
    expect(service.updateTweetComments).toBeDefined();
    expect(service.updateTweetComments(
      'Comment Message',
      'JohnDoe1234-9ebed2aa-c697-4d0a-8b08-991542fb9d4c',
      'JohnDoe1234-1af419cf-e313-4798-b944-3a8b464b3080'
    )).withContext('service to return Observable<TweetResponse>');
    expect(httpService.put.calls.count())
      .withContext('spy method was called once')
      .toBe(1);
  });

  it('should have deleteTweetComments function and called', () => {
    expect(service.deleteTweetComments).toBeDefined();
    expect(service.deleteTweetComments(
      'JohnDoe1234-9ebed2aa-c697-4d0a-8b08-991542fb9d4c',
      'JhonDoe1234'
    )).withContext('service to return Observable<TweetResponse>');
    expect(httpService.delete.calls.count())
      .withContext('spy method was called once')
      .toBe(1);
  });

  it('should have toggleCommentLike function and called', () => {
    expect(service.toggleCommentLike).toBeDefined();
    expect(service.toggleCommentLike({
      commentId: 'JohnDoe1234-9ebed2aa-c697-4d0a-8b08-991542fb9d4c',
      username: 'JhonDoe1234'
    })).withContext('service to return Observable<TweetResponse>');
    expect(httpService.put.calls.count())
      .withContext('spy method was called once')
      .toBe(1);
  });
});
