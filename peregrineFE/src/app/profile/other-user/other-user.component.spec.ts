import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatIconModule } from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { SharedModule } from 'src/app/shared/shared.module';

import { OtherUserComponent } from './other-user.component';

describe('OtherUserComponent', () => {
  let component: OtherUserComponent;
  let fixture: ComponentFixture<OtherUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OtherUserComponent],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        SharedModule,
        MatIconModule,
        HttpClientTestingModule
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OtherUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have redirect to be defined', () => {
    expect(component.redirect).toBeDefined();
  });
});