import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NG_ZORRO_MODULES } from '../../../../shared/config/ng-zorro.config';

@Component({
  selector: 'app-drawer-menu',
  standalone: true,
  imports: [
    ...NG_ZORRO_MODULES
  ],
  templateUrl: './drawer-menu.component.html',
  styleUrl: './drawer-menu.component.css'
})
export class DrawerMenuComponent {

  @Input() isCollapsed: boolean = false;
  @Output() hideMenuEmitter = new EventEmitter<boolean>();

  closeMenu(): void {
    this.hideMenuEmitter.emit(false);
  }

}
