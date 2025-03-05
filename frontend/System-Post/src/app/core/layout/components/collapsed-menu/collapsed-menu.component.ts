import { Component, Input } from '@angular/core';
import { NG_ZORRO_MODULES } from '../../../../shared/config/ng-zorro.config';

@Component({
  selector: 'app-collapsed-menu',
  standalone: true,
  imports: [
    ...NG_ZORRO_MODULES
  ],
  templateUrl: './collapsed-menu.component.html',
  styleUrl: './collapsed-menu.component.css'
})
export class CollapsedMenuComponent {

  @Input() isCollapsed: boolean = false;

}
