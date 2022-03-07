import { Injectable } from '@angular/core';
import { SessionStorageService as SessionService } from 'angular-web-storage';
import { CookieService } from 'ngx-cookie-service';
import { EncryptionService } from './encryption.service';

/**
 * Session Storage Service
 */
@Injectable({
  providedIn: 'root'
})
export class SessionStorageService {
  constructor(private sessionStorage: SessionService) { }

  /**
  * Store data in session storage
  * @param key the key against which data is to be stored
  * @param value the data to be stored
  */
  public setData(key: string, value: string): void {
    var encrypt = EncryptionService.encryptData(value);
    this.sessionStorage.set(key, encrypt);
  }

  /**
  * Get the data stored against a key from session storage
  * @param key the key against which data stored is to be retrieved
  * @returns the data stored against the key provided
  */
  public getData(key: string): string {
    var decrypt = this.sessionStorage.get(key);
    return EncryptionService.decryptData(decrypt);
  }

  /**
  * Delete the data stored against a key from session
  * @param key the key against which data stored needs to be deleted
  */
  public deleteData(key?: string): void {
    if (!!key)
      this.sessionStorage.remove(key);
    else
      this.sessionStorage.clear();
  }
}

/**
 * Cookie Storage Service
 */
@Injectable({
  providedIn: 'root'
})
export class CookieStorageService {
  constructor(private cookieService: CookieService) { }

  /**
   * Store data in cookie
   * @param key the key against which data is to be stored
   * @param value the data to be stored
   * @param expire the expiry date in mins
   */
  public setData(key: string, value: string, expire: number = 10): void {
    const date = new Date().setMinutes(new Date().getMinutes() + expire);
    var encrypt = EncryptionService.encryptData(value);
    this.cookieService.set(key, encrypt, date, '/', undefined, true, 'Strict');
  }

  /**
  * Get the data stored against a key from cookie
  * @param key the key against which data stored is to be retrieved
  * @returns the data stored against the key provided
  */
  public getData(key: string): string {
    var decrypt = this.cookieService.get(key);
    return EncryptionService.decryptData(decrypt);
  }

  /**
 * Delete the data stored against a key from cookie
 * @param key the key against which data stored needs to be deleted
 */
  public deleteData(key?: string): void {
    if (!!key)
      this.cookieService.delete(key);
    else
      this.cookieService.deleteAll();
  }

  /**
   * Check if key is present in Cookies
   * @param key the key against which the data is stored in the cookie
   * @returns boolean if a particular key exists in the cookie
   */
  public check(key: string): boolean {
    var cookieExists: boolean = this.cookieService.check(key);
    return cookieExists;
  }

  /**
   * [Not Recommended to Use] Get all data stored in the cookie
   * @returns the whole dataset of cookie
   */
  protected getAll() {
    let data = this.cookieService.getAll();
    var _this = this;
    Object.keys(data).forEach(function (keys) {
      data[keys] = _this.getData(keys);
    })
    return data;
  }
}

/**
 * Application Storage Service
 */
@Injectable({
  providedIn: 'root'
})
export class ApplicationStorageService {
  private __data: { [key: string]: string } = {};

  /**
   * Store data in application storage
   * @param key the key against which data is to be stored
   * @param value the data to be stored
   */
  public setData(key: string, value: string): void {
    this.__data[key] = value;
  }

  /**
   * Get the data stored against a key from application storage
   * @param key the key against which data stored is to be retrieved
   * @returns the data stored against the key provided
   */
  public getData(key: string): string {
    return this.__data[key];
  }

  /**
   * Delete the data stored against a key from application storage
   * @param key the key against which data stored needs to be deleted
   */
  public deleteData(key?: string): void {
    if (!!key)
      delete this.__data[key];
    else
      this.__data = {};
  }

  /**
   * [Not Recommended to Use] Get all data stored in the application storage
   * @returns the whole dataset of application storage
   */
  protected getAll(): { [key: string]: string } {
    return this.__data;
  }
}