import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { SharedModule } from '../shared/shared.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { OtherUserComponent } from './other-user/other-user.component';
import { ProfileServiceLazy } from './profile-lazy.module';
import { ProfileRoutingModule } from './profile-routing.module';
import { UserComponent } from './user/user.component';

@NgModule({
  declarations: [
    UserComponent,
    DashboardComponent,
    OtherUserComponent
  ],
  imports: [
    CommonModule,
    ProfileRoutingModule,
    MatIconModule,
    FormsModule,
    ProfileServiceLazy,
    SharedModule,
    HttpClientModule
  ]
})
export class ProfileModule { }
