import { Component } from '@angular/core';
import { NG_ZORRO_MODULES } from './shared/config/ng-zorro.config';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    ...NG_ZORRO_MODULES
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  
}
