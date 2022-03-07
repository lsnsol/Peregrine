import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Profile } from 'src/app/models/profile.model';
import { HttpService } from 'src/app/services/http.service';
import { RegistrationService } from './registration.service';

describe('RegistrationService', () => {
  let service: RegistrationService;
  let httpService: jasmine.SpyObj<HttpService>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('HttpService', ['post']);
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        RegistrationService,
        {
          provide: HttpService,
          useValue: spy
        }
      ]
    });
    service = TestBed.inject(RegistrationService);
    httpService = TestBed.inject(HttpService) as jasmine.SpyObj<HttpService>;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should have register function and called', () => {
    const profile: Profile = {
      firstName: 'John',
      lastName: 'Doe',
      username: 'JohnDoe1234',
      email: 'john.doe@email.com',
      password: '123@Qwerty',
      avatar: 'man',
      contactNumber: '1234567890'
    };
    expect(service.register).toBeDefined();
    expect(service.register(profile)).withContext('service to return Observable<MessageOnlyResponse>')
    expect(httpService.post.calls.count())
      .withContext('spy method was called once')
      .toBe(1);
  });

  it('should have validateUsername function and called', () => {
    expect(service.validateUsername).toBeDefined();
    expect(service.validateUsername('JohnDoe1234')).withContext('service to return Observable<UsernameValidation>')
    expect(httpService.post.calls.count())
      .withContext('spy method was called once')
      .toBe(1);
  });

  it('should have validateEmail function and called', () => {
    expect(service.validateEmail).toBeDefined();
    expect(service.validateEmail('john.doe@email.com')).withContext('service to return Observable<EmailValidation>')
    expect(httpService.post.calls.count())
      .withContext('spy method was called once')
      .toBe(1);
  });
});
