import { TestBed } from '@angular/core/testing';
import { take } from 'rxjs';
import { Profile } from '../models/profile.model';
import { DataService } from './data.service';
import { EncryptionService } from './encryption.service';


describe('DataService', () => {
  let service: DataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EncryptionService]
    });
    service = TestBed.inject(DataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should be false initially and set loginState as value and recieve from getter and observable', async () => {
    expect(service.loginState).toBeFalse();
    service.getLoginStatusObs().pipe(take(1)).subscribe(data => {
      expect(data).toBeFalse();
    });
    const setValue = true;
    service.loginState = setValue;
    expect(service.loginState).toBe(setValue);
    service.getLoginStatusObs().subscribe(data => {
      expect(data).toBe(setValue);
    });
  });

  it('should be null initially and set profile as value and recieve from getter and observable', async () => {
    expect(service.profile).toBeNull();
    service.getProfileObs().pipe(take(1)).subscribe(data => {
      expect(data).toBeNull();
    });
    const setValue: Profile = {
      firstName: 'John',
      lastName: 'Doe',
      username: 'JohnDoe1234',
      email: 'john.doe@email.com',
      password: '123@Qwerty',
      avatar: 'man',
      contactNumber: '1234567890'
    };
    service.profile = setValue;
    expect(service.profile).toBe(setValue);
    service.getProfileObs().subscribe(data => {
      expect(data).toBe(setValue);
    });
  });

  it('should be null initially and set jwt as value and recieve from getter and observable', async () => {
    expect(service.jwt).toEqual('');
    service.getJWTObs().pipe(take(1)).subscribe(data => {
      expect(data).toEqual('');
    });
    const setValue = 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdWFyMjIxMjEzMTEiLCJleHAiOjE2NDM4MDQ2NDksImlhdCI6MTY0MzE5OTg0OX0.SVg4Z3vr2aKnO-YX7OPWEmZ3OCHodXPryEqQlOdhyTcWr1bDCQbMvBgBf2PalrqPJbKS_S8nEUUM9TJ02RmCag';
    service.jwt = setValue;
    expect(service.jwt).toBe(setValue);
    service.getJWTObs().subscribe(data => {
      expect(data).toBe(setValue);
    });
  });
});
