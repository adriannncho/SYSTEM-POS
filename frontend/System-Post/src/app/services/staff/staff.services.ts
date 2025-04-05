import { Injectable } from "@angular/core";
import { environment } from "../../enviroments/environment.local";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { User } from "../../interfaces/staff/staff.interface";

@Injectable({
    providedIn: 'root'
  })
  export class StaffService {
  
    private readonly apiUrl: string = environment.apiUrlSP.sp_procesor;
  
    constructor(
      private http: HttpClient,
    ) { }
  
    getStaff(): Observable<User[]>{
        return this.http.get<User[]>(`${this.apiUrl}/user`);
    }
  }