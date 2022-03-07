import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatIconModule } from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';

import { UserComponent } from './user.component';
import { UserService } from './user.service';

describe('UserComponent', () => {
  let component: UserComponent;
  let fixture: ComponentFixture<UserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports:[
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientTestingModule,
        MatIconModule
      ],
      declarations: [UserComponent],
      providers: [
        UserService
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have userNameClickEvent to be defined', () => {
    expect(component.userNameClickEvent).toBeDefined();
  });

  it('should have editPass to be defined', () => {
    expect(component.editPass).toBeDefined();
  });
  
  it('should have showProfile to be defined', () => {
    expect(component.showProfile).toBeDefined();
  });
  
  it('should have redirect to be defined', () => {
    expect(component.redirect).toBeDefined();
  });
});
