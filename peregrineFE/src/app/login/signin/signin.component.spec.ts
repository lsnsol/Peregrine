import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpService } from 'src/app/services/http.service';

import { SigninComponent } from './signin.component';
import { SigninService } from './signin.service';

describe('SigninComponent', () => {
  let component: SigninComponent;
  let fixture: ComponentFixture<SigninComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule, 
        RouterTestingModule,
        ReactiveFormsModule,
        FormsModule,
        BrowserAnimationsModule
      ],
      declarations: [SigninComponent],
      providers: [
        SigninService,
        HttpService
      ]
    })
      .compileComponents();
  });

  const formFieldArray = [
    'username',
    'password',
  ];

  const formFieldArrayValues = [
    'JohnDoe1234',
    '123@Qwerty'
  ];


  beforeEach(() => {
    fixture = TestBed.createComponent(SigninComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('form should be invalid when empty', () => {
    expect(component.loginForm.valid).toBeFalsy();
  });

  formFieldArray.forEach((formField, i) => {
    it(`${formField} initially to be invalid, have null value and show validators`, () => {
      expect(component.loginForm.controls[formField].value).toBeFalsy();
      expect(component.loginForm.controls[formField].valid).toBeFalsy();
      expect((component.loginForm.controls[formField].errors || {})['required']).toBeTruthy();
    });

    it(`${formField} value to be valid and required to be fullfilled`, () => {
      component.loginForm.controls[formField].setValue(formFieldArrayValues[i]);
      expect(component.loginForm.controls[formField].value).toBeTruthy();
      expect((component.loginForm.controls[formField].errors || {})['required']).toBeFalsy();
    });
  });
});
