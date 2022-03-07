import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { HttpErrorInterceptor } from './http-interceptor.service';

describe('HttpErrorInterceptor', () => {
  let service: HttpErrorInterceptor;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule]
    });
    service = TestBed.inject(HttpErrorInterceptor);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should have intercept funtion', () => {
    expect(service.intercept).toBeDefined();
  })
});
