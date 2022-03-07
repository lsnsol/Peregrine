import { TestBed } from '@angular/core/testing';

import { EncryptionService } from './encryption.service';

describe('EncryptionService', () => {
  let service: EncryptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EncryptionService]
    });
    service = TestBed.inject(EncryptionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should have function encrypt function defined', ()=>{
    expect(EncryptionService.encryptData).toBeDefined();
  });

  it('should have function decrypt function defined', ()=>{
    expect(EncryptionService.decryptData).toBeDefined();
  });

  it('should encrypt data', () => {
    const encryptedData = EncryptionService.encryptData('test');
    expect(encryptedData).not.toEqual('test');
  });

  it('should decrypt data', () => {
    const encryptedData = EncryptionService.encryptData('test');
    expect(EncryptionService.decryptData(encryptedData)).toEqual('test');
  });
});
