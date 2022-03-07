import { HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { BASE_URL } from '../models/api.constants';

import { HttpService } from './http.service';


describe('HttpService', () => {
  let service: HttpService;
  let httpMock: HttpTestingController;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(HttpService);
    httpMock = TestBed.get(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should have get defined', () => {
    expect(service.post).toBeDefined();
    service.get('getToken').subscribe();
    const req = httpMock.expectOne(BASE_URL + 'getToken');
    expect(req.request.method).toEqual('GET');
    httpMock.verify();
  });

  it('should have post defined', () => {
    expect(service.post).toBeDefined();
    service.post('getToken', {}).subscribe();
    const req = httpMock.expectOne(BASE_URL + 'getToken');
    expect(req.request.method).toEqual('POST');
    httpMock.verify();
  });

  it('should have put defined', () => {
    expect(service.put).toBeDefined();
    service.put('getToken', {}).subscribe();
    const req = httpMock.expectOne(BASE_URL + 'getToken');
    expect(req.request.method).toEqual('PUT');
    httpMock.verify();
  });

  it('should have delete defined', () => {
    expect(service.delete).toBeDefined();
    service.delete('getToken', {}).subscribe();
    const req = httpMock.expectOne(BASE_URL + 'getToken');
    expect(req.request.method).toEqual('DELETE');
    httpMock.verify();
  });
});
