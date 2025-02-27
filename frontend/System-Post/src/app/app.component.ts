import { Component } from '@angular/core';
import { NG_ZORRO_MODULES } from './shared/config/ng-zorro.config';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    ...NG_ZORRO_MODULES
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  
}
