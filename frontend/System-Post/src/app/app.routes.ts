import { Routes } from '@angular/router';
import { BaseComponent } from './core/layout/components/base/base.component';
import { AuthGuard } from './core/guards/auth.guard';
import { AuthPublicGuard } from './core/guards/auth-public.guard';
import { StaffComponent } from './modules/private/pages/staff/staff.component';
import { MenuComponent } from './modules/private/pages/menu/menu/menu.component';

export const routes: Routes = [

  {
    path: 'signin',
    loadComponent: () => import('./modules/public/pages/login/login.component').then(m => m.LoginComponent),
    canActivate: [AuthPublicGuard], 
    data: { preload: true },
    title: 'Iniciar Sesión' 
  },
  {
    path: '',
    component: BaseComponent,
    data: { preload: false },
    canActivate : [AuthGuard],
    children: [
      {
        path: 'intranet/staff',
        component: StaffComponent,
        path: 'intranet/menu',
        component: MenuComponent,
        data: {preload: true}
      }
  }

];
