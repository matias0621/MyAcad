import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router'; // 👈 Importamos Router
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators'; // 👈 Importamos catchError
import { NotificationService } from '../notification/notification.service';

@Injectable({
  providedIn: 'root'
})
export class JwtInterceptor implements HttpInterceptor {

  constructor(private router: Router, private notificationService: NotificationService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');

    let requestToHandle = req;

    if (token) {
      requestToHandle = req.clone({
        setHeaders: { 
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(requestToHandle).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          console.error('Token expirado o inválido. Redirigiendo a inicio de sesión.');
          
          localStorage.removeItem('token'); 
          this.router.navigate(['/auth/login']); 
          this.notificationService.warning("Tu sesión ha expirado", true);
        }
        return throwError(() => error); 
      })
    );
  }
}