import { Injectable } from '@angular/core';
import * as SHA from 'sha.js';

@Injectable()
export class HashService {

  /**
   * Hash a given string with given algo
   * @param value value to be hashed
   * @param algo algorithm used to be hashed
   * @returns hashed string
   */
  private static hashFunction(value: string, algo: SHA.Algorithm = 'sha256'): string {
    return SHA(algo).update(value).digest('hex');
  }

  /**
   * Hash function for use in application
   * @param value value to be hashed
   * @param algo algorithm used to be hashed
   * @returns hashed string
   */
  public static hash(value: string, algorithm?: SHA.Algorithm): string {
    if (!!value)
      return this.hashFunction(value, algorithm);
    return '';
  }
}
