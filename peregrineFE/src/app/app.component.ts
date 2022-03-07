import { AfterContentInit, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { debounceTime } from 'rxjs';
import { Profile } from './models/profile.model';
import { LOGIN_ROUTE } from './models/routes.constant';
import { DataService } from './services/data.service';
import { Alert } from './shared/alert/alert.component';
import { AlertService } from './shared/alert/alert.service';
import { LoaderService } from './shared/loader/loader.service';

@Component({
  selector: 'peregrine-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterContentInit {
  public logoutBtn: boolean = false;
  public loader: boolean = true;
  public alertArray: Alert[] = [];
  public profile: Profile | null = null;

  constructor(
    private dataService: DataService,
    private router: Router,
    private alertService: AlertService,
    private loaderService: LoaderService
  ) { }

  ngOnInit() {
    this.dataService.getLoginStatusObs().subscribe((state: boolean) => {
      this.logoutBtn = state;
    });
    this.dataService.getProfileObs().subscribe((profile: Profile | null) => {
      this.profile = profile;
    });
  }

  ngAfterContentInit(): void {
    this.alertService.getAlertObs().pipe(
      debounceTime(500)
    ).subscribe(alertData => {
      this.alertArray = [alertData]
    });
    this.loaderService.getLoaderObs().pipe(
      debounceTime(500)
    ).subscribe(loader => {
      this.loader = loader;
    });
  }

  logout() {
    this.dataService.reset();
    this.router.navigateByUrl(LOGIN_ROUTE);
  }
}
