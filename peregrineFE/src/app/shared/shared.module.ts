import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AlertComponent } from './alert/alert.component';
import { LoaderComponent } from './loader/loader.component';
import { TweetComponent } from './tweet/tweet.component';
import { TweetService } from './tweet/tweet.service';

const SharedModuleList = [
  AlertComponent,
  LoaderComponent,
  TweetComponent
]

@NgModule({
  declarations: [...SharedModuleList],
  imports: [
    CommonModule,
    NgbModule,
    FormsModule,
    NgbModule,
    MatIconModule,
    MatCardModule
  ],
  exports: [...SharedModuleList]
})
export class SharedModule { }
