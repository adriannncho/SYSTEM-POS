import { Component, OnInit } from '@angular/core';
import { NG_ZORRO_MODULES } from '../../../../../shared/config/ng-zorro.config';
import { CategoriesProductsService } from '../../../../../services/categorias/categories-products.service';
import { CategoriesProductsResp } from '../../../../../interfaces/categorias-products/categorias-products.interface';
import { NotificationService } from '../../../../../core/services/notification/notification.service';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [
    ...NG_ZORRO_MODULES,
  ],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit{

  loadingCategories: boolean = false;
  categoriesProducts: CategoriesProductsResp[] = [];
  labelsCategories: string[] = [];

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
        this.categoriesProducts = categories;
        this.getLabelsCtaegories();
      } else {
        this.notificationService.error("No se encontraron categorias disponibles", "Cargar categorias");
      }
      this.loadingCategories = false;
    }, (error => {
      this.loadingCategories = false;
      this.notificationService.error("Ocurrio un erro al cargar la ctaegorias disponibles", "Cargar categorias")
    }));
  }

  getLabelsCtaegories(): void {
    this.categoriesProducts.forEach(item => {
      this.labelsCategories.push(item.name);
    })
  }

}
