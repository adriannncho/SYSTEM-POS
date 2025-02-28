import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NG_ZORRO_MODULES } from '../../../../shared/config/ng-zorro.config';

@Component({
  selector: 'app-base',
  standalone: true,
 imports: [
    RouterOutlet,
    ...NG_ZORRO_MODULES
  ],
  templateUrl: './base.component.html',
  styleUrl: './base.component.css'
})
export class BaseComponent {

}
