import { Injectable } from '@angular/core';
import * as CryptoJS from 'crypto-js';

const ENCRYPTION_KEY = '__PeregrineApp__';

@Injectable()
export class EncryptionService {

  public static encryptData(value: string): string {
    return CryptoJS.AES.encrypt(value, ENCRYPTION_KEY).toString();
  }

  public static decryptData(value: string): string {
    return CryptoJS.AES.decrypt(value, ENCRYPTION_KEY).toString(CryptoJS.enc.Utf8);
  }
}
