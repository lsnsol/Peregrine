import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LOG_I_1020 } from 'src/app/models/error.constants';
import { MessageOnlyResponse, PASSWORD_REGEX } from 'src/app/models/general.model';
import { Profile } from 'src/app/models/profile.model';
import { OTHER_USER_ROUTE, PROFILE_ROUTE } from 'src/app/models/routes.constant';
import { DataService } from 'src/app/services/data.service';
import { HashService } from 'src/app/services/hash.service';
import { AlertService } from 'src/app/shared/alert/alert.service';
import { LoaderService } from 'src/app/shared/loader/loader.service';
import { UserService } from './user.service';

@Component({
  selector: 'peregrine-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css'],
  animations: [
    trigger("simpleFadeAnimation", [
      state("in", style({ opacity: 1 })),
      transition(":enter", [style({ opacity: 0 }), animate(1500)]),
      transition(":leave", animate(1000, style({ opacity: 0 })))
    ])
  ]
})
export class UserComponent implements OnInit {
  public profile?: Profile;
  public showprofile: boolean = false;
  public editPasswordFlag: boolean = false;
  public editPasswordText: string = '';
  public editPasswordConfirmText: string = '';

  constructor(
    private dataService: DataService,
    private userService: UserService,
    private router: Router,
    private loaderService: LoaderService,
    private alertService: AlertService
  ) { }

  ngOnInit(): void {
    if (this.dataService.profile)
      this.profile = this.dataService.profile;
    this.loaderService.loader = true;
  }

  /**
   * Show hide profile details
   */
  public showProfile() {
    this.showprofile = !this.showprofile;
  }

  /**
   * Edit Password for the user
   */
  public editPass() {
    if (this.profile && this.editPasswordText === this.editPasswordConfirmText && PASSWORD_REGEX.test(this.editPasswordText)) {
      this.loaderService.loader = true;
      const editPasswordObserver = {
        observer: this,
        next(message: MessageOnlyResponse) {
          if (message.messages[0].message === LOG_I_1020) {
            this.observer.alertService.alertMessage('Password Changed Successfully', 'green', 1000);
            this.observer.editPasswordFlag = false;
          }
          else
            this.observer.alertService.alertMessage('Password Change Failed', 'yellow');
          this.observer.loaderService.loader = false;
        },
        error() {
          this.observer.alertService.alertMessage('Server error, cannot change password');
        }
      }
      this.userService.editPassword(this.profile.username, HashService.hash(this.editPasswordText)).subscribe(editPasswordObserver);
    }
    else if (this.editPasswordText != this.editPasswordConfirmText)
      this.alertService.alertMessage('Password do not match');
    else if (!PASSWORD_REGEX.test(this.editPasswordText))
      this.alertService.alertMessage('Password doest not match pattern');
  }

  /**
   * Navigate to a given url
   */
  public redirect(route: string) {
    this.router.navigateByUrl(route);
  }

  /**
   * Event click handler
   * @param data username and avatar emitted from the event to further send to tweet component
   */
  public userNameClickEvent(data: { username: string, avatar: string }) {
    this.router.navigate([PROFILE_ROUTE + '/' + OTHER_USER_ROUTE, data]);
  }
}
