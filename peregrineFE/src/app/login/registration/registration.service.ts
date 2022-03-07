import { Injectable, NgModule } from '@angular/core';
import { Observable } from 'rxjs';
import { REGISTER_URL, VALIDATE_EMAIL_URL, VALIDATE_USERNAME_URL } from 'src/app/models/api.constants';
import { IkeyValuePair, MessageOnlyResponse } from 'src/app/models/general.model';
import { EmailValidation, Profile, UsernameValidation } from 'src/app/models/profile.model';
import { HttpService } from 'src/app/services/http.service';

@NgModule()
export class RegistrationServiceLazy { }

@Injectable({
  providedIn: RegistrationServiceLazy
})
export class RegistrationService {

  constructor(private http: HttpService) { }

  /**
   * Registration of user
   * @param profile profile data to be registered
   * @returns Observable with Registration Response
   */
  public register(profile: Profile): Observable<MessageOnlyResponse> {
    return this.http.post<Profile, MessageOnlyResponse>(REGISTER_URL, { ...profile });
  }

  /**
   * Validation of username for a new registration of user
   * @param username username to be checked
   * @returns Observable with UsernameValidationResponse
   */
  public validateUsername(username: string): Observable<UsernameValidation> {
    return this.http.post<IkeyValuePair<string>, UsernameValidation>(VALIDATE_USERNAME_URL, { username });
  }

  /**
   * Validation of email for a new registration of user
   * @param email email to be checked
   * @returns Observable with UsernameValidationResponse
   */
  public validateEmail(email: string): Observable<EmailValidation> {
    return this.http.post<IkeyValuePair<string>, EmailValidation>(VALIDATE_EMAIL_URL, { email })
  }
}