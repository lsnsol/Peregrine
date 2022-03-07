import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { map, Observable } from 'rxjs';
import { LOGIN_ROUTE } from '../models/routes.constant';
import { DataService } from './data.service';

@Injectable({
  providedIn: 'root'
})
export class LoginGuard implements CanActivate {

  constructor(private dataService: DataService, private router: Router) { }

  /**
   * Login guard for routes to be activated only for logged in users
   * @returns Observable with boolean value true for loggedIn user
   */
  canActivate(): Observable<boolean> {
    return this.dataService.getLoginStatusObs().pipe(map((activationStatus: boolean) => {
      if (activationStatus)
        return true
      this.router.navigateByUrl(LOGIN_ROUTE);
      return false;
    }));
  }
}
