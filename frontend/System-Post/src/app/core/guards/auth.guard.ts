import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { AuthenticationService } from "../services/authentication/authentication.service";
import { NotificationService } from "../services/notification/notification.service";

@Injectable ({
  providedIn: 'root'
})

export class AuthGuard {

  constructor (
    private authService: AuthenticationService,
    private router: Router,
    private notificationService : NotificationService
  ) {}

  canActivate() {
    let isLogged: boolean = false;
    if(this.authService.isAuthenticated() && this.authService.isTokenValid()) {
      isLogged = true;
    } else {
      this.authService.logout();
      isLogged = false;
      this.router.navigate(['/signin'])
      this.notificationService.warning('Es necesario Iniciar sesión para acceder a esta ruta');
    }
    return isLogged;
  }
}