import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthenticationService } from '../../services/authentication/authentication.service';
import { NotificationService } from '../../services/notification/notification.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthenticationService);
  const notificationService = inject(NotificationService);
  const token = authService.getToken();
  const infoToken = authService.getInformationUserAuth();

  if (req.url.includes('/auth/login')) {
    return next(req);
  }

  if (token) {
    const now = Date.now() / 1000;
    const tokenExp = infoToken?.exp ?? 0;

    if (tokenExp - now < 600 && tokenExp - now > 0) {
      if (!authService.isRefreshing) {
        authService.startRefreshing();
        authService.refreshToken().subscribe(res => {
          if (res) {
            authService.setToken(res.refresh_token);
          }
          authService.stopRefreshing();
        });
      }
    } else if (tokenExp <= now) {
      authService.logout();
      notificationService.warning("Su sesión ha caducado. Es necesario iniciar sesión nuevamente.")
    }

    const authReq = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });

    return next(authReq);
  }

  return next(req);
};
