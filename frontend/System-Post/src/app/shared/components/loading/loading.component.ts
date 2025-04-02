import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-loading',
  standalone: true,
  imports: [],
  templateUrl: './loading.component.html',
  styleUrl: './loading.component.css'
})
export class LoadingComponent {

  @Input() show: boolean = false; // Controla la visibilidad
  @Input() message: string = 'Cargando...'; // Mensaje opcional
  @Input() overlay: boolean = true; // Controla si muestra un fondo oscuro

}
