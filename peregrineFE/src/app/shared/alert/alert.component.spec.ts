import { ComponentFixture, TestBed } from "@angular/core/testing";
import { AlertComponent } from "./alert.component";

describe('AlertComponent', () => {
  let component: AlertComponent;
  let fixture: ComponentFixture<AlertComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        AlertComponent
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AlertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should have defined triggerAlert function defined', () => {
    expect(component.triggerAlert).toBeDefined();
  })
});
