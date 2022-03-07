import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { HttpService } from 'src/app/services/http.service';

import { SigninService } from './signin.service';

describe('SigninService', () => {
  let service: SigninService;
  let httpService: jasmine.SpyObj<HttpService>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('HttpService', ['post']);
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        SigninService,
        {
          provide: HttpService,
          useValue: spy
        }
      ]
    });
    service = TestBed.inject(SigninService);
    httpService = TestBed.inject(HttpService) as jasmine.SpyObj<HttpService>;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should have signIn function and called', () => {
    expect(service.siginin).toBeDefined();
    expect(service.siginin('JohnDoe1234', '123@Qwerty')).withContext('service to return Observable<SigninResponse>')
    expect(httpService.post.calls.count())
      .withContext('spy method was called once')
      .toBe(1);
  });
});
