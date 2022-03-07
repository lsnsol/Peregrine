import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatIconModule } from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { SharedModule } from 'src/app/shared/shared.module';

import { DashboardComponent } from './dashboard.component';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        MatIconModule,
        SharedModule
      ],
      declarations: [DashboardComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have refreshTweet defined', () => {
    expect(component.refreshTweets).toBeDefined();
  });

  it('should have goToProfile defined', () => {
    expect(component.gotoProfile).toBeDefined();
  });
  
  it('should have userNameClickEvent to be defined', () => {
    expect(component.userNameClickEvent).toBeDefined();
  });

  it('should have profile value set from data service', () => {
    expect(component.profile).toBeNull();
  });
});
