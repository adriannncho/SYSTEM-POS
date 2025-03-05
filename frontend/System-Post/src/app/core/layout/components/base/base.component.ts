import { Component, HostListener } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NG_ZORRO_MODULES } from '../../../../shared/config/ng-zorro.config';
import { CollapsedMenuComponent } from '../collapsed-menu/collapsed-menu.component';
import { DrawerMenuComponent } from '../drawer-menu/drawer-menu.component';

@Component({
  selector: 'app-base',
  standalone: true,
  imports: [
    RouterOutlet, 
    CollapsedMenuComponent,
    DrawerMenuComponent,
     ...NG_ZORRO_MODULES
    ],
  templateUrl: './base.component.html',
  styleUrl: './base.component.css',
})
export class BaseComponent {

  width = window.innerWidth;
  isCollapsed: boolean = false;

  showMenu(): void {
    this.isCollapsed = !this.isCollapsed;
  }

  /**
   * Escucha los eventos del cambio del ancho de la pantalla y asigna el valor de width
   * @param event Evento con los cambios en el ancho de la pantallla
   */
  @HostListener('window:resize', ['$event']) onResize(event: Event) {
    if (event.target instanceof Window) {
      this.width = event.target.innerWidth;
    }
  }
}
