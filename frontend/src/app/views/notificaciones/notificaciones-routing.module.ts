import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotificacionesComponent } from './notificaciones.component';
import { NotificacionesListComponent } from './notificaciones-list/notificaciones-list.component';

const routes: Routes = [
  { 
    path: '', 
    component: NotificacionesComponent,
    children:[
      { path:'', component: NotificacionesListComponent },
      { path:'list', component: NotificacionesListComponent }
    ]
  }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class NotificacionesRoutingModule { }
