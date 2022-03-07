import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LOG_I_1017, REG_I_1014 } from 'src/app/models/error.constants';
import { MessageOnlyResponse } from 'src/app/models/general.model';
import { Profile } from 'src/app/models/profile.model';
import { LOGIN_ROUTE, PROFILE_ROUTE } from 'src/app/models/routes.constant';
import { DataService } from 'src/app/services/data.service';
import { HashService } from 'src/app/services/hash.service';
import { AlertService } from 'src/app/shared/alert/alert.service';
import { LoaderService } from 'src/app/shared/loader/loader.service';
import { RegistrationValidators } from './registration-validators';
import { RegistrationService } from './registration.service';

@Component({
  selector: 'peregrine-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css'],
  providers: [RegistrationValidators, HashService],
  animations: [
    trigger("simpleFadeAnimation", [
      state("in", style({ opacity: 1 })),
      transition(":enter", [style({ opacity: 0 }), animate(1500)]),
      transition(":leave", animate(1000, style({ opacity: 0 })))
    ])
  ]
})
export class RegistrationComponent {

  public loader: boolean = false;
  public readonly avatarsList = ['boy', 'coder', 'coder2', 'death', 'girl', 'hacker', 'man', 'woman', 'oldman', 'oldwoman'];
  public registrationForm: FormGroup = new FormGroup({
    firstName: new FormControl(null, {
      validators: [Validators.required, RegistrationValidators.validateStringNullOrMinLength(2, 'First Name')], updateOn: 'blur'
    }),
    lastName: new FormControl(null, {
      validators: [Validators.required, RegistrationValidators.validateStringNullOrMinLength(2, 'Last Name')],
      updateOn: 'blur'
    }),
    username: new FormControl(null, {
      validators: [Validators.required, RegistrationValidators.validateStringNullOrMinLength(2, 'User Name')],
      asyncValidators: [RegistrationValidators.validateUsername(this.registrationService)],
      updateOn: 'blur'
    }),
    email: new FormControl(null, {
      validators: [Validators.required, Validators.email],
      asyncValidators: [RegistrationValidators.validateEmail(this.registrationService)],
      updateOn: 'blur'
    }),
    phoneNo: new FormControl(null, {
      validators: [Validators.required, RegistrationValidators.validatePhoneNo()],
      updateOn: 'blur'
    }),
    password: new FormControl(null, {
      validators: [Validators.required, RegistrationValidators.validatePassword()],
      updateOn: 'blur'
    }),
    confirmPassword: new FormControl(null, {
      validators: [Validators.required, RegistrationValidators.validatePassword()],
      updateOn: 'blur'
    }),
    avatar: new FormControl(null, {
      validators: [Validators.required],
      updateOn: 'blur'
    })
  }, { validators: RegistrationValidators.validateSignUpForm() });

  constructor(
    private registrationService: RegistrationService,
    private registrationValidator: RegistrationValidators,
    private router: Router,
    private dataService: DataService,
    private loaderService: LoaderService,
    private alertService: AlertService
  ) {
    this.registrationForm.statusChanges.subscribe(status => {
      if (status === 'INVALID') {
        const message = this.findErrorInForm(this.registrationForm);
        if (message)
          this.alertService.alertMessage(message);
      }
    });
  }

  /**
   * validate the errors in the form
   */
  public validate(): void {
    this.alertService.alert = {};
    const error = this.registrationForm.errors;
    if (!!error) {
      this.alertService.alertMessage(error['invalid']);
    }
  }

  /**
   * redirect to login page
   */
  public loginRedirect(route: string = LOGIN_ROUTE) {
    this.router.navigateByUrl(route)
  }

  /**
   * signup function to perform the signup after validations
   */
  public signup(): void {
    this.loaderService.loader = true;
    const _user = this.createUserData(this.registrationForm);
    if (!!_user) {
      const registerObserver = {
        observer: this,
        next(data: MessageOnlyResponse) {
          if (data.messages[0].message === REG_I_1014) {
            this.observer.loaderService.loader = false;
            this.observer.dataService.profile = _user;
            if (data.messages[1].message === LOG_I_1017) {
              this.observer.dataService.loginState = true;
              this.observer.router.navigateByUrl(PROFILE_ROUTE);
            }
          }
          else {
            this.observer.loaderService.loader = false;
            this.observer.alertService.alertMessage('User not registered, please try again');
          }
        },
        error() {
          this.observer.loaderService.loader = false;
          this.observer.alertService.alertMessage('Server error, user not registered, please try again');
        }
      };
      this.registrationService.register(_user).subscribe(registerObserver);
    }
  }

  /**
   * Find which field has error in the form
   * @param form the registration form
   * @returns message string or null
   */
  private findErrorInForm(form: FormGroup): string | null {
    let msg = '';
    if (form.get('firstName')?.errors)
      msg = form.get('firstName')?.errors?.['invalid'];
    else if (form.get('lastName')?.errors)
      msg = form.get('lastName')?.errors?.['invalid'];
    else if (form.get('email')?.errors)
      msg = form.get('email')?.errors?.['invalid'];
    else if (form.get('username')?.errors)
      msg = form.get('username')?.errors?.['invalid'];
    else if (form.get('password')?.errors)
      msg = form.get('password')?.errors?.['invalid'];
    else if (form.get('confirmPassword')?.errors)
      msg = form.get('confirmPassword')?.errors?.['invalid'];
    else if (form.get('phoneNo')?.errors)
      msg = form.get('phoneNo')?.errors?.['invalid'];
    else if (form.get('avatar')?.errors)
      msg = 'Select an avatar';
    return msg;
  }

  /**
   * Creates the new user from the given form
   * @param user the registration form
   * @returns new user profile or undefined
   */
  private createUserData(user: FormGroup): Profile | undefined {
    const _user: Profile = {
      firstName: '',
      lastName: '',
      username: '',
      email: '',
      contactNumber: '',
      password: '',
      avatar: 'default'
    };
    _user.firstName = user.get('firstName')?.value;
    _user.lastName = user.get('lastName')?.value;
    _user.username = user.get('username')?.value;
    _user.email = user.get('email')?.value;
    _user.contactNumber = user.get('phoneNo')?.value;
    _user.password = HashService.hash(user.get('password')?.value);
    if (user.get('avatar')?.value)
      _user.avatar = user.get('avatar')?.value;
    return _user;
  }
}
