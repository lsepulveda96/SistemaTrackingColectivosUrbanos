import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ViewsComponent } from './views.component';

const routes: Routes = [
  { 
    path: '', 
    component: ViewsComponent,
    children: [
      { path: 'usuarios', loadChildren: () => import('./usuarios/usuarios.module').then(m => m.UsuariosModule) },
      { path: 'colectivos', loadChildren: () => import('./colectivos/colectivos.module').then(m => m.ColectivosModule) },
      { path: 'paradas', loadChildren: () => import('./paradas/paradas.module').then(m => m.ParadasModule) },
      { path: 'lineas', loadChildren: () => import('./lineas/lineas.module').then(m => m.LineasModule) },
      { path: 'monitoreo', loadChildren: () => import('./monitoreo/monitoreo.module').then(m => m.MonitoreoModule) },
      { path: 'notificaciones', loadChildren: () => import('./notificaciones/notificaciones.module').then(m => m.NotificacionesModule) },
    ]
   },
  
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ViewsRoutingModule { }
