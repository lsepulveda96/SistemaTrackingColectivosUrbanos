import { Routes } from '@angular/router';

import { FullComponent } from './layouts/full/full.component';

export const AppRoutes: Routes = [
  {
    path: '',
    component: FullComponent,
    children: [
      {
        path: '',
        redirectTo: '/dashboard',
        pathMatch: 'full'
      },
      /*
      {
        path: '',
        loadChildren:
          () => import('./material-component/material.module').then(m => m.MaterialComponentsModule)
      },
      */
     {
       path: '',
       loadChildren: () => import('./views/views.module').then( m => m.ViewsModule )
     },
      {
        path: 'dashboard',
        loadChildren: () => import( './views/principal/principal.module').then( m=> m.PrincipalModule )
      },
      {
        path: 'auth',
        loadChildren: () => import( './auth/auth.module').then( m => m.AuthModule )
      }
    ]
  }
];
