import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { AuthenticationService } from "../services/authentication/authentication.service";

@Injectable ({
  providedIn: 'root'
})

export class AuthGuard {

  constructor (
    private authService: AuthenticationService,
    private router: Router,
    //private notificationService : NotificationService
  ) {}

  canActivate() {
    let isLogged: boolean = false;
    if(this.authService.isAuthenticated()) {
      isLogged = true;
    } else {
      isLogged = false;
      this.router.navigate(['/signin'])
      //this.notificationService.warning('Es necesario Iniciar sesión para acceder a esta ruta');
    }
    return isLogged;
  }
}