import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../enviroments/environment.local';
import { Observable } from 'rxjs';
import { GroupsAuth, JwtDecodeUser, LoginBody, ResponseLogin } from '../../models/authentication/auth.interface';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private readonly apiUrl: string = environment.apiUrlSP.sp_procesor;

  constructor(
    private http: HttpClient,
  ) { }

  signinWhithIdentificationAndPassword(body: LoginBody): Observable<ResponseLogin> {
    return this.http.post<ResponseLogin>(`${this.apiUrl}/login`, body);
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
}
