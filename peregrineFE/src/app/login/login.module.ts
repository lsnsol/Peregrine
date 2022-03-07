import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatSelectModule } from '@angular/material/select';
import { SharedModule } from '../shared/shared.module';
import { LoginRoutingModule } from './login-routing.module';
import { RegistrationComponent } from './registration/registration.component';
import { RegistrationServiceLazy } from './registration/registration.service';
import { SigninComponent } from './signin/signin.component';
import { SigninServiceLazy } from './signin/signin.service';


@NgModule({
  declarations: [
    RegistrationComponent,
    SigninComponent
  ],
  imports: [
    CommonModule,
    LoginRoutingModule,
    MatCardModule,
    MatSelectModule,
    FormsModule,
    ReactiveFormsModule,
    SigninServiceLazy,
    RegistrationServiceLazy,
    HttpClientModule,
    SharedModule
  ],
  providers: [HttpClient]
})
export class LoginModule { }
