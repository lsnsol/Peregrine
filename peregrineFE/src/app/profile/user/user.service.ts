import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { POST_TWEET_URL, UPDATE_PASSWORD, USER_TWEET_URL } from 'src/app/models/api.constants';
import { MessageOnlyResponse } from 'src/app/models/general.model';
import { Profile, ResetPassword } from 'src/app/models/profile.model';
import { PostTweetRequest, Tweet, TweetResponse, TweetWithComments, UsernameTweetRequest } from 'src/app/models/tweet.model';
import { HttpService } from 'src/app/services/http.service';
import { ProfileServiceLazy } from '../profile-lazy.module';

@Injectable({
  providedIn: ProfileServiceLazy
})
export class UserService {

  constructor(private http: HttpService) { }

  /**
   * Edit password function
   * @param username username
   * @param password new password
   * @returns Observable<MessageOnlyResponse> from the server
   */
  public editPassword(username: string, password: string): Observable<MessageOnlyResponse> {
    return this.http.put<ResetPassword, MessageOnlyResponse>(UPDATE_PASSWORD, { username, password });
  }
}
