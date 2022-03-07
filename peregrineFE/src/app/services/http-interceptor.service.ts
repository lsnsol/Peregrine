import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { LOGIN_ROUTE } from '../models/routes.constant';

@Injectable({
  providedIn: 'root'
})
export class HttpErrorInterceptor implements HttpInterceptor {
  constructor(private router: Router) { }

  /**
   * Intercept HTTP request and response for errors if any which is thrown and redirect to login page
   * @param request Request which is sent to REST client
   * @param next HttpHandler for handling the request and response for errors if any
   * @returns Observable<HttpEvent<any>> for errors to be further handled in component specific level
   */
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request)
      .pipe(
        retry(1),
        catchError((error: HttpErrorResponse) => {
          if (error.status === 401 || error.status === 403)
            this.router.navigateByUrl(LOGIN_ROUTE);
          return throwError(error);
        })
      );
  }
}