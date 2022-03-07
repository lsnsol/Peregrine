import { Injectable, NgModule } from '@angular/core';
import { Observable } from 'rxjs';
import { LOGIN_URL } from 'src/app/models/api.constants';
import { SigninResponse } from 'src/app/models/profile.model';
import { HttpService } from 'src/app/services/http.service';

@NgModule()
export class SigninServiceLazy { }

@Injectable({
  providedIn: SigninServiceLazy
})
export class SigninService {

  constructor(private http: HttpService) { }

  /**
   * Login/Signin via signin URL 
   * @param username username for signin
   * @param password password for signin
   * @returns Observable with Signin Response from backend
   */
  public siginin(username: string, password: string): Observable<SigninResponse> {
    return this.http.post<{ username: string, password: string }, SigninResponse>(LOGIN_URL, { username, password });
  }
}
