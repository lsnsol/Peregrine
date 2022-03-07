import { TestBed } from '@angular/core/testing';
import { take } from 'rxjs';

import { LoaderService } from './loader.service';

describe('LoaderService', () => {
  let service: LoaderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoaderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should be false initially and set loader as value and recieve from getter and observable', () => {
    expect(service.loader).toBeFalse();
    service.getLoaderObs().pipe(take(1)).subscribe(data => {
      expect(data).toBeFalse();
    });
  });

  it('should set loader as value and recieve from getter and observable', () => {
    const setValue = true;
    service.loader = setValue;
    expect(service.loader).toBe(setValue);
    service.getLoaderObs().subscribe(data => {
      expect(data).toBe(setValue);
    });
  })
});
