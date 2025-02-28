import { Routes } from '@angular/router';
import { BaseComponent } from './core/layout/components/base/base.component';

export const routes: Routes = [

  {
    path: '',
    component: BaseComponent,
    data: { preload: false },
    //canActivate : [AuthGuard]
  }

];
