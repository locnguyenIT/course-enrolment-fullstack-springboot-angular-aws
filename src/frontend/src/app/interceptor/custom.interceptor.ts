import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HTTP_INTERCEPTORS,
  HttpErrorResponse
} from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { AuthService } from '../service/auth.service';

@Injectable()
export class CustomInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    const token = this.authService.getToken();
    if(token !== null) {
      request = request.clone( {
        setHeaders: { Authorization: 'Bearer '+token}
      });
    }
    return next.handle(request);

  }
}

export const HttpInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: CustomInterceptor, multi:true}
];
