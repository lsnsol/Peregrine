import { Injectable } from "@angular/core";
import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";
import { map, Observable, of } from "rxjs";
import { PASSWORD_REGEX, USERNAME_REGEX } from "src/app/models/general.model";
import { RegistrationService } from "./registration.service";

export interface AsyncValidatorFn {
  (c: AbstractControl): Observable<ValidationErrors | null>
}

@Injectable()
export class RegistrationValidators {

  /**
   * Validate the middle name field in the signup form
   * @param minLen minimun length or null
   * @returns validator function with the validation status
   */
  public static validateStringNullOrMinLength(minLen: number, formField: string = 'Form field'): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const dataToValidate: string = control.value;
      let valid = false;
      if (dataToValidate === null || (dataToValidate && dataToValidate.length >= minLen))
        valid = true;
      return !valid ? { invalid: `${formField} with value '${control.value}' Doesn't meet minimum length requirement` } : null;
    }
  }

  /**
   * Validate the password field in the signup form
   * @returns validator function with the validation status
   */
  public static validatePassword(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const dataToValidate: string = control.value;
      let valid = false;
      if (!!dataToValidate && PASSWORD_REGEX.test(dataToValidate))
        valid = true;
      return !valid ? { invalid: 'Password not matching conditions' } : null;
    }
  }

  /**
   * Validate the password field in the signup form
   * @returns validator function with the validation status
   */
  public static validatePhoneNo(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const dataToValidate: string = control.value;
      let valid = false;
      if (dataToValidate && dataToValidate.length === 10)
        valid = true;
      return !valid ? { invalid: 'Invalid Phone Number' } : null;
    }
  }

  /**
   * Validate the income validity in the signup form
   * @returns validator function with the validation status
   */
  public static validateSignUpForm(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const password: string = control.value.password;
      const confirmPassword: string = control.value.confirmPassword;
      const validPassword = password === confirmPassword;
      let invalid = null;
      if (!validPassword) {
        invalid = 'Password Mismatch';
        control.get('confirmPassword')?.setErrors({ invalid: 'Password doesn\'t match' });
      }
      return validPassword ? null : { invalid };
    }
  }

  /**
   * Validate the username entered is duplicate or not
   * @param registrationService RegistrationService instance
   * @returns async validator function with validation status
   */
  public static validateUsername(registrationService: RegistrationService): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      const dataToValidate: string = control.value + '';
      let valid = false;
      if (!!dataToValidate && USERNAME_REGEX.test(dataToValidate)) {
        return registrationService.validateUsername(dataToValidate).pipe(map((data) => {
          if (!data.usernameExists)
            valid = true;
          control.get('username')?.setErrors({ invalid: 'Username Taken' });
          return !valid ? { invalid: 'Username Taken' } : null;
        }));
      } else
        return !valid ? of({ invalid: 'Username does not match pattern' }) : of(null);
    }
  }

  /**
   * Validate the email entered is duplicate or not
   * @param registrationService RegistrationService instance
   * @returns async validator function with validation status
   */
  public static validateEmail(registrationService: RegistrationService): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      const dataToValidate: string = control.value + '';
      let valid = false;
      return registrationService.validateEmail(dataToValidate).pipe(map((data) => {
        if (!data.emailExists)
          valid = true;
        control.get('email')?.setErrors({ invalid: 'Email Used' });
        return !valid ? { invalid: 'Email Used' } : null;
      }));
    }
  }
}