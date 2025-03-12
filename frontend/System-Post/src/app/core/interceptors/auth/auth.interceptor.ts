import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthenticationService } from '../../services/authentication/authentication.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const authService = inject(AuthenticationService);
  const token = authService.getToken();

  // Excluir endpoints públicos (opcional)
  if (req.url.includes('/auth/login')) {
    return next(req);
  }

  // Clonar request y agregar token
  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(authReq);
  }

  return next(req);
};