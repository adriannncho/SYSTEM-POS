import { Injectable } from '@angular/core';
import { environment } from '../../enviroments/environment.local';
import { HttpClient } from '@angular/common/http';
import { CategoriesProductsResp } from '../../interfaces/categorias-products/categorias-products.interface';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoriesProductsService {

  private readonly apiUrl: string = environment.apiUrlSP.sp_procesor;

  constructor(
    private http: HttpClient,
  ) { }

  getCategoriesProducts(): Observable<CategoriesProductsResp[]> {
    return this.http.get<CategoriesProductsResp[]>(`${this.apiUrl}/category`);
  }
}
