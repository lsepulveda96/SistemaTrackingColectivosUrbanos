import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NotificacionesRoutingModule } from './notificaciones-routing.module';
import { NotificacionesComponent } from './notificaciones.component';


@NgModule({
  declarations: [
    NotificacionesComponent
  ],
  imports: [
    CommonModule,
    NotificacionesRoutingModule
  ]
})
export class NotificacionesModule { }
