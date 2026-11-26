import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { AuthenticationService } from '../authentication';
import { CurrentUserService } from '../current-user';


export const authenticationInterceptor: HttpInterceptorFn = (request, next) => {
  if (!request.url.startsWith(environment.api_endpoint)) {
    return next(request);
  }

  const authenticationService = inject(AuthenticationService);
  const token = authenticationService.getToken();

  if (token === null) {
    return next(request);
  }

  const router = inject(Router);
  const currentUserService = inject(CurrentUserService);

  const authorizedRequest = request.clone({
    setHeaders: { Authorization: `Bearer ${token}` },
  });

  return next(authorizedRequest).pipe(
    catchError((error: unknown) => {
      if (error instanceof HttpErrorResponse && (error.status === 401 || error.status === 403)) {
        authenticationService.logout();
        currentUserService.clear();
        router.navigate(['/account/sign-in']);
      }
      return throwError(() => error);
    }),
  );
};
