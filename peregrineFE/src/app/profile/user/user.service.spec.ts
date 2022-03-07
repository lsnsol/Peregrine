import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { HttpService } from 'src/app/services/http.service';

import { UserService } from './user.service';

describe('UserService', () => {
  let service: UserService;
  let httpService: jasmine.SpyObj<HttpService>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('HttpService', ['put']);
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        UserService,
        {
          provide: HttpService,
          useValue: spy
        }
      ]
    });
    service = TestBed.inject(UserService);
    httpService = TestBed.inject(HttpService) as jasmine.SpyObj<HttpService>;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should have editPassword function and called', () => {
    expect(service.editPassword).toBeDefined();
    expect(service.editPassword('JohnDoe1234', '123@Qwerty')).withContext('service to return Observable<MessageOnlyResponse>')
    expect(httpService.put.calls.count())
      .withContext('spy method was called once')
      .toBe(1);
  });
});
