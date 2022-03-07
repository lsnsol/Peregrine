import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TweetComponent } from './tweet.component';

describe('TweetComponent', () => {
  let component: TweetComponent;
  let fixture: ComponentFixture<TweetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TweetComponent]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TweetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have addNewTweet function defined', () => {
    expect(component.addNewTweet).toBeDefined();
  });

  it('should have editTweetButton function defined', () => {
    expect(component.editTweetButton).toBeDefined();
    component.editTweetButton(0);
    expect(component.editTweetFlag[0]).toBeTrue();
  });

  it('should have updateTweet function defined', () => {
    expect(component.updateTweet).toBeDefined();
  });

  it('should have cancelEditTweet function defined', () => {
    expect(component.cancelEditTweet).toBeDefined();
    component.cancelEditTweet(0);
    expect(component.editTweetFlag[0]).toBeFalse();
  });

  it('should have deleteTweet function defined', () => {
    expect(component.deleteTweet).toBeDefined();
  });

  it('should have toggleTweetLike function defined', () => {
    expect(component.toggleTweetLike).toBeDefined();
  });

  it('should have toggleComments function defined', () => {
    expect(component.toggleComments).toBeDefined();
  });

  it('should have addComment function defined', () => {
    expect(component.addComment).toBeDefined();
  });

  it('should have clearCommentInput function defined', () => {
    expect(component.clearCommentInput).toBeDefined();
    component.clearCommentInput(0);
    expect(component.newComment[0]).toBe('');
  });

  it('should have editComment function defined', () => {
    expect(component.editComment).toBeDefined();
  });

  it('should have editCommentConfirm function defined', () => {
    expect(component.editCommentConfirm).toBeDefined();
  });

  it('should have deleteComment function defined', () => {
    expect(component.deleteComment).toBeDefined();
  });

  it('should have toggleCommentLike function defined', () => {
    expect(component.toggleCommentLike).toBeDefined();
  });

  it('should have handleKeyboardEvent function defined', () => {
    expect(component.handleKeyboardEvent).toBeDefined();
  });
  
  it('should have userNameClickEvent to be defined', () => {
    expect(component.userNameClickEvent).toBeDefined();
  });
});
