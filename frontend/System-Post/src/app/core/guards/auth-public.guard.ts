import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { AuthenticationService } from "../services/authentication/authentication.service";
import { NotificationService } from "../services/notification/notification.service";


@Injectable ({
  providedIn: 'root'
})

export class AuthPublicGuard {

  constructor (
    private authService: AuthenticationService,
    private router: Router,
    private notificationService: NotificationService
  ) {}

  canActivate() {
    if(this.authService.isAuthenticated()) {
      this.router.navigate([''])
      this.notificationService.warning('Es necesario cerrar sesión para acceder a esta ruta');
      return false;
    }else {
      return true;
    }
  }
}