import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../helpers/auth.guard';
import { MapComponent } from './map/map.component';
import { ViewsComponent } from './views.component';
import { AccessRoutesGuard } from '../helpers/access-routes.guard';

const routes: Routes = [
  { 
    path: '', 
    component: ViewsComponent,
    children: [
      { 
        path: 'usuarios', 
        loadChildren: () => import('./usuarios/usuarios.module').then(m => m.UsuariosModule),
        canActivate: [AccessRoutesGuard,AuthGuard] 
      },
      { 
        path: 'colectivos', 
        loadChildren: () => import('./colectivos/colectivos.module').then(m => m.ColectivosModule),
        canActivate: [AuthGuard] 
      },
      { 
        path: 'paradas', 
        loadChildren: () => import('./paradas/paradas.module').then(m => m.ParadasModule),
        canActivate: [AuthGuard] 
      },
      { 
        path: 'lineas', 
        loadChildren: () => import('./lineas/lineas.module').then(m => m.LineasModule),
        canActivate: [AuthGuard] 
      },
      { 
        path: 'monitoreo', 
        loadChildren: () => import('./monitoreo/monitoreo.module').then(m => m.MonitoreoModule),
        canActivate: [AuthGuard] 
      },
      { 
        path: 'notificaciones', 
        loadChildren: () => import('./notificaciones/notificaciones.module').then(m => m.NotificacionesModule),
        canActivate: [AuthGuard] 
      },
      { 
        path: 'principal', 
        loadChildren: () => import('./principal/principal.module').then(m => m.PrincipalModule) 
      },
      { 
        path: 'perfil', 
        loadChildren: () => import('./perfil/perfil.module').then(m => m.PerfilModule) },
      { 
        path:'map', component: MapComponent
      },
    ]
   },
  { path: 'misc', loadChildren: () => import('./misc/misc.module').then(m => m.MiscModule) },
  
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ViewsRoutingModule { }
