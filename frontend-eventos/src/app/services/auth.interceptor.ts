import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent
} from '@angular/common/http';
import { Observable, from } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private auth: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // sólo añadir token a rutas que comienzan con /api (evitar añadir a requests externas)
    if (!req.url.startsWith('/api')) {
      return next.handle(req);
    }

    // convertir la Promise de token a Observable y clonar request con header
    return from(this.auth.getToken()).pipe(
      switchMap(token => {
        if (token) {
          const headers = req.headers.set('Authorization', `Bearer ${token}`);
          const cloned = req.clone({ headers });
          return next.handle(cloned);
        } else {
          return next.handle(req);
        }
      })
    );
  }
}
