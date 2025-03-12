import { Routes } from '@angular/router';
import { BaseComponent } from './core/layout/components/base/base.component';

export const routes: Routes = [

  {
    path: 'signin',
    loadComponent: () => import('./modules/public/pages/login/login.component').then(m => m.LoginComponent),
    //canActivate: [authPublicGuard], 
    data: { preload: true },
    title: 'Iniciar Sesión' 
  },
  {
    path: '',
    component: BaseComponent,
    data: { preload: false },
    //canActivate : [AuthGuard]
  }

];
