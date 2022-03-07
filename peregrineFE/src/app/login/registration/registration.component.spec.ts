import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpService } from 'src/app/services/http.service';
import { RegistrationValidators } from './registration-validators';
import { RegistrationComponent } from './registration.component';
import { RegistrationService } from './registration.service';


describe('RegistrationComponent', () => {
  let component: RegistrationComponent;
  let fixture: ComponentFixture<RegistrationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        BrowserAnimationsModule,
        FormsModule,
        MatSelectModule,
        ReactiveFormsModule
      ],
      declarations: [RegistrationComponent],
      providers: [
        RegistrationService,
        RegistrationValidators,
        HttpService
      ]
    })
      .compileComponents();
  });

  const formFieldArray = [
    'firstName',
    'lastName',
    'username',
    'email',
    'phoneNo',
    'password',
    'confirmPassword',
    'avatar'
  ];

  const formFieldArrayValues = [
    'John',
    'Doe',
    'JohnDoe1234',
    'john.doe@email.com',
    '1234567890',
    '123@Qwerty',
    '123@Qwerty',
    'man'
  ];

  beforeEach(() => {
    fixture = TestBed.createComponent(RegistrationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('form should be invalid when empty', () => {
    expect(component.registrationForm.valid).toBeFalsy();
  });

  formFieldArray.forEach((formField, i) => {
    it(`${formField} initially to be invalid, have null value and show validators`, () => {
      expect(component.registrationForm.controls[formField].value).toBeFalsy();
      expect(component.registrationForm.controls[formField].valid).toBeFalsy();
      expect((component.registrationForm.controls[formField].errors || {})['required']).toBeTruthy();
    });

    it(`${formField} value to be valid and required to be fullfilled`, () => {
      component.registrationForm.controls[formField].setValue(formFieldArrayValues[i]);
      expect(component.registrationForm.controls[formField].value).toBeTruthy();
      expect((component.registrationForm.controls[formField].errors || {})['required']).toBeFalsy();
    });
  });
});
