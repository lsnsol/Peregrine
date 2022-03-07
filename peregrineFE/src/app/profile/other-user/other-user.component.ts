import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Avatars } from 'src/app/models/profile.model';
import { DataService } from 'src/app/services/data.service';

@Component({
  selector: 'peregrine-other-user',
  templateUrl: './other-user.component.html',
  styleUrls: ['./other-user.component.css'],
  animations: [
    trigger("simpleFadeAnimation", [
      state("in", style({ opacity: 1 })),
      transition(":enter", [style({ opacity: 0 }), animate(1500)]),
      transition(":leave", animate(1000, style({ opacity: 0 })))
    ])
  ]
})
export class OtherUserComponent implements OnInit {
  public avatar: Avatars = 'default';
  public username: string = '';
  public currentUser: string = '';

  constructor(private router: Router, private route: ActivatedRoute, private dataService: DataService) { }

  ngOnInit(): void {
    this.route.params.forEach(data => {
      this.username = data['username'];
      this.avatar = data['avatar'];
    });
    this.currentUser = this.dataService.profile?.username + '';
  }

  redirect(path: string) {
    this.router.navigateByUrl(path)
  }
}
