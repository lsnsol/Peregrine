import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Profile } from 'src/app/models/profile.model';
import { OTHER_USER_ROUTE, PROFILE_ROUTE, USER_ROUTE } from 'src/app/models/routes.constant';
import { DataService } from 'src/app/services/data.service';
import { LoaderService } from 'src/app/shared/loader/loader.service';

@Component({
  selector: 'peregrine-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  animations: [
    trigger("simpleFadeAnimation", [
      state("in", style({ opacity: 1 })),
      transition(":enter", [style({ opacity: 0 }), animate(1500)]),
      transition(":leave", animate(1000, style({ opacity: 0 })))
    ])
  ]
})
export class DashboardComponent implements OnInit {
  public profile: Profile | null;

  constructor(
    private dataService: DataService,
    private router: Router,
    private loaderService: LoaderService
  ) {
    this.profile = this.dataService.profile;
  }

  ngOnInit(): void {
    this.loaderService.loader = true;
  }

  /**
   * Navigate to profile
   */
  public gotoProfile() {
    this.router.navigateByUrl(PROFILE_ROUTE + '/' + USER_ROUTE);
  }

  /**
   * refresh tweets and get updated list
   */
  public refreshTweets() {
    this.loaderService.loader = true;
    this.profile = null;
    setTimeout(() => {
      this.profile = this.dataService.profile;
      this.loaderService.loader = false;
    }, 1000);
  }

  /**
   * Event click handler
   * @param data username and avatar emitted from the event to further send to tweet component
   */
  public userNameClickEvent(data: { username: string, avatar: string }) {
    if (this.profile?.username === data.username)
      this.router.navigateByUrl(PROFILE_ROUTE + '/' + USER_ROUTE);
    else
      this.router.navigate([PROFILE_ROUTE + '/' + OTHER_USER_ROUTE, data]);
  }
}

