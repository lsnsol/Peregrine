import { TestBed } from '@angular/core/testing';
import { EncryptionService } from './encryption.service';

import { ApplicationStorageService, CookieStorageService, SessionStorageService } from './storage.service';

describe('SessionStorageService', () => {
  let sessionStorageService: SessionStorageService;
  let cookieStorageService: CookieStorageService;
  let applicationStorageService: ApplicationStorageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EncryptionService]
    });
    sessionStorageService = TestBed.inject(SessionStorageService);
    cookieStorageService = TestBed.inject(CookieStorageService);
    applicationStorageService = TestBed.inject(ApplicationStorageService);
  });

  it('should be created', () => {
    expect(sessionStorageService).toBeTruthy();
    expect(sessionStorageService.getData).toBeDefined();
    expect(sessionStorageService.setData).toBeDefined();
    expect(sessionStorageService.deleteData).toBeDefined();

    expect(cookieStorageService).toBeTruthy();
    expect(cookieStorageService.getData).toBeDefined();
    expect(cookieStorageService.setData).toBeDefined();
    expect(cookieStorageService.deleteData).toBeDefined();

    expect(applicationStorageService).toBeTruthy();
    expect(applicationStorageService.getData).toBeDefined();
    expect(applicationStorageService.setData).toBeDefined();
    expect(applicationStorageService.deleteData).toBeDefined();
  });

  it(`set data, get data, delete data, from session storage`, () => {
    sessionStorageService.setData('key', 'value');
    expect(sessionStorageService.getData('key')).toEqual('value');
    sessionStorageService.deleteData();
    try {
      sessionStorageService.getData('key');
    } catch (e) {
      expect(e).toBeTruthy();
    }
  });

  it(`set data, get data, delete data, from cookie storage`, () => {
    cookieStorageService.setData('key', 'value');
    expect(cookieStorageService.getData('key')).toEqual('value');
    cookieStorageService.deleteData();
    expect(cookieStorageService.getData('key')).toEqual('');
  });

  it(`set data, get data, delete data, from application`, () => {
    applicationStorageService.setData('key', 'value');
    expect(applicationStorageService.getData('key')).toEqual('value');
    applicationStorageService.deleteData();
    expect(applicationStorageService.getData('key')).toBeUndefined();
  });
});
