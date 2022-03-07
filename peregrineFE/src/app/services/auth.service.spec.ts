import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { LoginGuard } from './auth.service';

describe('LoginGuard', () => {
  let service: LoginGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule]
    });
    service = TestBed.inject(LoginGuard);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should have login guard', () => {
    expect(service.canActivate).toBeDefined();
  });
});
