import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Profile } from '../models/profile.model';
import { CookieStorageService } from './storage.service';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private storageService: CookieStorageService) { }

  // Login Status
  private loginStatusBS = new BehaviorSubject<boolean>(this.storageService.getData('loginState') === 'true');
  private readonly loginStatusObs = this.loginStatusBS.asObservable();

  /**
   * Set login state
   * @param state the state as true or false
   */
  set loginState(state: boolean) {
    this.loginStatusBS.next(state);
    this.storageService.setData('loginState', state + '');
  }

  /**
   * Get login state
   * @returns the state as true or false
   */
  get loginState(): boolean { return this.loginStatusBS.value; }

  /**
   * Get the login state as Observable
   * @returns Observale with login state 
   */
  getLoginStatusObs(): Observable<boolean> { return this.loginStatusObs; }


  // Profile Data
  private profileBS = new BehaviorSubject<Profile | null>((this.storageService.getData('profile') != '') ? JSON.parse(this.storageService.getData('profile')) : null);
  private readonly profileObs = this.profileBS.asObservable();

  /**
   * Set profile data
   * @param profile the profile data
   */
  set profile(profile: Profile | null) {
    this.profileBS.next(profile);
    this.storageService.setData('profile', JSON.stringify(profile));
  }

  /**
   * Get profile data
   * @returns the profile data
   */
  get profile(): Profile | null { return this.profileBS.value; }

  /**
   * Get the profile data as observable
   * @returns Observale with profile data
   */
  getProfileObs(): Observable<Profile | null> { return this.profileObs; }


  // JWT Data
  private jwtBS = new BehaviorSubject<string | null>(this.storageService.getData('jwt'));
  private readonly jwtObs = this.jwtBS.asObservable();

  /**
   * Set jwt data
   * @param jwt the jwt data
   */
  set jwt(jwt: string | null) {
    this.jwtBS.next(jwt);
    this.storageService.setData('jwt', jwt + '');
  }

  /**
   * Get jwt data
   * @returns the jwt data
   */
  get jwt(): string | null { return this.jwtBS.value; }

  /**
   * Get the jwt data as observable
   * @returns Observale with jwt data
   */
  getJWTObs(): Observable<string | null> { return this.jwtObs; }

  /**
   * Reset all the data to initial value
   */
  public reset() {
    this.jwt = '';
    this.profile = null;
    this.loginState = false;
    this.storageService.deleteData();
  }
}
