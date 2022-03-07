import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { BASE_URL } from '../models/api.constants';
import { HTTP_REQUEST_HEADER, IkeyValuePair } from '../models/general.model';
import { DataService } from './data.service';

@Injectable({
  providedIn: 'root'
})
export class HttpService {
  constructor(private http: HttpClient, private dataService: DataService) { }

  /**
   * Create and modify HttpHeaders for REST call
   * @param header HttpHeaders if any or null
   * @param jwt use jwt or not boolean value
   * @returns {Headers: IkeyValuePair<string>, observe:'response'} for headers 
   */
  private createOption(header: HttpHeaders | null = null, jwt: boolean = true): { headers: IkeyValuePair<string>, observe: 'response' } {
    let headers: IkeyValuePair<string> = HTTP_REQUEST_HEADER;
    if (header) headers = { ...headers, ...header };
    if (jwt) headers = { ...headers, 'Authorization': this.dataService.jwt + '' };
    const observe = 'response';
    return { headers, observe };
  }

  /**
   * HTTP GET Call with return type ReT
   * @param url URL to call (don't include BASE_URL)
   * @param header additional HTTP Headers if required
   * @returns 
   */
  public get<ReT>(url: string, header?: HttpHeaders, jwt: boolean = true): Observable<ReT> {
    url = jwt ? BASE_URL + url : url;
    return this.http.get(url, { ...this.createOption(header, jwt) }).pipe(map((data) => {
      this.dataService.jwt = data.headers.get('Authorization') ? data.headers.get('Authorization') : this.dataService.jwt;
      return data.body as ReT;
    }));
  }

  /**
   * HTTP POST Call with request type RqT and response type ReT
   * @param url URL to call (don't include BASE_URL)
   * @param payload payload with request type ReT as specified
   * @param header additional HTTP Headers if required
   * @returns 
   */
  public post<RqT, ReT>(url: string, payload: RqT, header?: HttpHeaders, jwt: boolean = true): Observable<ReT> {
    url = jwt ? BASE_URL + url : url;
    return this.http.post(url, payload, { ...this.createOption(header, jwt) }).pipe(map(data => {
      this.dataService.jwt = data.headers.get('Authorization') ? data.headers.get('Authorization') : this.dataService.jwt;
      return data.body as ReT;
    }));
  }

  /**
   * HTTP PUT Call  with request type RqT and response type ReT
   * @param url URL to call (don't include BASE_URL)
   * @param payload payload with request type ReT as specified
   * @param header additional HTTP Headers if required
   * @returns 
   */
  public put<RqT, ReT>(url: string, payload: RqT, header?: HttpHeaders, jwt: boolean = true): Observable<ReT> {
    url = jwt ? BASE_URL + url : url;
    return this.http.put(url, payload, { ...this.createOption(header, jwt) }).pipe(map(data => {
      this.dataService.jwt = data.headers.get('Authorization') ? data.headers.get('Authorization') : this.dataService.jwt;
      return data.body as ReT;
    }));
  }

  /**
   * HTTP DELETE Call with request type RqT and return type ReT
   * @param url URL to call (don't include BASE_URL)\
   * @param header additional HTTP Headers if required
   * @returns 
   */
  public delete<RqT, ReT>(url: string, body: RqT, header?: HttpHeaders, jwt: boolean = true): Observable<ReT> {
    url = jwt ? BASE_URL + url : url;
    return this.http.delete(url, { ...this.createOption(header, jwt), body }).pipe(map(data => {
      this.dataService.jwt = data.headers.get('Authorization') ? data.headers.get('Authorization') : this.dataService.jwt;
      return data.body as ReT;
    }));
  }
}
