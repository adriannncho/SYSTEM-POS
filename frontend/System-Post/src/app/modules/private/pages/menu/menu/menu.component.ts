import { Component, OnInit } from '@angular/core';
import { NG_ZORRO_MODULES } from '../../../../../shared/config/ng-zorro.config';
import { CategoriesProductsService } from '../../../../../services/categorias/categories-products.service';
import { CategoriesProductsResp } from '../../../../../interfaces/categorias-products/categorias-products.interface';
import { NotificationService } from '../../../../../core/services/notification/notification.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [
    ...NG_ZORRO_MODULES,
    CommonModule
  ],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit{

  loadingCategories: boolean = false;
  categoriesProducts: CategoriesProductsResp[] = [];
  selectedCategoryId: number = 0;
  allCategoriesOption: CategoriesProductsResp = {
    idCategory: 0,
    name: 'Todas',
    icon: 'product', /* Pendiente icono todas las categorias */
    selected: true
  }

  constructor(
    private categoriesService: CategoriesProductsService,
    private notificationService: NotificationService
  ) {

  }
  ngOnInit(): void {
    this.getCategoriesProducts();
  }

  /**
   * Obtiene las categorias de los productos
   */
  getCategoriesProducts(): void {
    this.loadingCategories = true;
    this.categoriesService.getCategoriesProducts().subscribe(categories => {
      if (categories && categories.length > 0) {
        categories.unshift(this.allCategoriesOption);
        this.categoriesProducts = categories;
        this.selectedCategoryId = this.categoriesProducts[0]?.idCategory ?? 0;
      } else {
        this.notificationService.error("No se encontraron categorias disponibles", "Cargar categorias");
      }
      this.loadingCategories = false;
    }, (error => {
      this.loadingCategories = false;
      this.notificationService.error("Ocurrio un erro al cargar la ctaegorias disponibles", "Cargar categorias")
    }));
  }

  seletedCategory (category: CategoriesProductsResp): void {
    this.categoriesProducts.forEach(cat => {
      if (cat.idCategory === category.idCategory) {
        this.selectedCategoryId = category.idCategory;
        cat.selected = true;
      } else {
        cat.selected = false;
      }
    });
  }

}
