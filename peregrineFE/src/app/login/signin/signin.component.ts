import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LOG_I_1017, LOG_W_1015 } from 'src/app/models/error.constants';
import { SigninResponse } from 'src/app/models/profile.model';
import { LOGIN_ROUTE, PROFILE_ROUTE, REGISTER_ROUTE } from 'src/app/models/routes.constant';
import { DataService } from 'src/app/services/data.service';
import { HashService } from 'src/app/services/hash.service';
import { Alert } from 'src/app/shared/alert/alert.component';
import { AlertService } from 'src/app/shared/alert/alert.service';
import { LoaderService } from 'src/app/shared/loader/loader.service';
import { SigninService } from './signin.service';

@Component({
  selector: 'peregrine-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css'],
  providers: [HashService],
  animations: [
    trigger("simpleFadeAnimation", [
      state("in", style({ opacity: 1 })),
      transition(":enter", [style({ opacity: 0 }), animate(1500)]),
      transition(":leave", animate(1000, style({ opacity: 0 })))
    ])
  ]
})
export class SigninComponent {
  public loader: boolean = false;
  public alertArray: Alert[] = [];
  public loginForm: FormGroup = new FormGroup({
    username: new FormControl(null, [Validators.required]),
    password: new FormControl(null, [Validators.required])
  });

  constructor(
    private router: Router,
    private signinService: SigninService,
    private dataService: DataService,
    private alertService: AlertService,
    private loaderService: LoaderService
  ) { 
    this.dataService.reset();
  }

  /**
   * Login based on credentials
   */
  public login(): void {
    this.loaderService.loader = true;
    const username = this.loginForm.get('username')?.value;
    const password = HashService.hash(this.loginForm.get('password')?.value);
    if (!!username && !!password) {
      const signinObserver = {
        observer: this,
        next(data: SigninResponse) {
          this.observer.loaderService.loader = false;
          if (data.messages && data.messages[0].message === LOG_I_1017 && data.profile) {
            this.observer.dataService.loginState = true;
            this.observer.dataService.profile = data.profile;
            this.observer.router.navigateByUrl(PROFILE_ROUTE);
          }
          else if (data.messages[0].message === LOG_W_1015)
            this.observer.alertService.alertMessage('User Not Found');
          else
            this.observer.alertService.alertMessage('Login unsuccesful, please try again');
        },
        error() {
          this.observer.loaderService.loader = false;
          this.observer.alertService.alertMessage('Server Error, login unsuccesful, please try again');
        }
      };
      this.signinService.siginin(username, password).subscribe(signinObserver);
    }
    else {
      this.loaderService.loader = false;
      this.alertService.alertMessage('Enter Valid Credentials', 'yellow');
    }
  }

  /**
   * Redirect to signup page
   */
  public signup() {
    this.router.navigateByUrl(LOGIN_ROUTE + '/' + REGISTER_ROUTE);
  }
}
