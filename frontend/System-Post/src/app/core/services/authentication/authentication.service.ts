import { HttpClient } from '@angular/common/http';
import { Injectable, signal, WritableSignal } from '@angular/core';
import { environment } from '../../../enviroments/environment.local';
import { Observable } from 'rxjs';
import { GroupsAuth, JwtDecodeUser, LoginBody, ResponseLogin, ResponseRefresh } from '../../models/authentication/auth.interface';
import { jwtDecode } from 'jwt-decode';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private readonly apiUrl: string = environment.apiUrlSP.sp_procesor;

  isRefreshing: WritableSignal<boolean> = signal(false);

  constructor(
    private http: HttpClient,
    private router: Router,
  ) { }

  signinWhithIdentificationAndPassword(body: LoginBody): Observable<ResponseLogin> {
    return this.http.post<ResponseLogin>(`${this.apiUrl}/login`, body);
  }

  setToken(token: string) {
    localStorage.setItem('token', token);
  }

  startRefreshing() {
    this.isRefreshing.set(true);
  }

  stopRefreshing() {
    this.isRefreshing.set(false);
  }

  getInformationUserAuth(): JwtDecodeUser {
    const token = localStorage.getItem('token');
    let infoUserDefault: JwtDecodeUser = {
      iss: '',
      sub: '',
      document_number: '',
      business_id: '',
      iat: 0,
      exp: 0,
      groups: []
    }

    if (token) {
      try {
        const decodedToken = jwtDecode(token);
        return decodedToken as JwtDecodeUser;
      } catch (error) {
        return infoUserDefault;
      }
    }
    return infoUserDefault;
  }

  whoIsLogged(): GroupsAuth {
    const infoUser: JwtDecodeUser | null = this.getInformationUserAuth();
    
    // Si no hay usuario autenticado o no tiene grupos
    if (!infoUser?.groups?.length) {
      return GroupsAuth.NOT_ROLE;
    }
  
    // Convertimos los valores del enum a un array de strings
    const validRoles = Object.values(GroupsAuth);
    
    // Buscamos el primer rol válido en los grupos del usuario
    const userRole = infoUser.groups.find(
      (group: string) => validRoles.includes(group as GroupsAuth)
    );
  
    // Retornamos el rol encontrado o NOT_ROLE por defecto
    return userRole ? (userRole as GroupsAuth) : GroupsAuth.NOT_ROLE;
  }

  getToken(): string | null {
    return localStorage.getItem("token");
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  /**
   * Realiza la peticion a back para obtener el token refrescado
   * @returns Token refrescado
   */
  refreshToken(): Observable<ResponseRefresh> {
    return this.http.post<ResponseRefresh>(`${this.apiUrl}/refresh`, null);
  }

  logout () {
    localStorage.removeItem('token');
    this.router.navigate(['/signin'])
  }
}
