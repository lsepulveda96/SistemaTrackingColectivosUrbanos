import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NotificacionesRoutingModule } from './notificaciones-routing.module';
import { NotificacionesComponent } from './notificaciones.component';
import { NotificacionesListComponent } from './notificaciones-list/notificaciones-list.component';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatDividerModule } from '@angular/material/divider';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { MatNativeDateModule } from '@angular/material/core';
import { MatTooltipModule } from '@angular/material/tooltip';
import { NotificacionUbicacionComponent } from './notificaciones-ubicacion/notificaciones-ubicacion.component';
import { MatListModule } from '@angular/material/list';


@NgModule({
  declarations: [
    NotificacionesComponent,
    NotificacionesListComponent,
    NotificacionUbicacionComponent
  ],
  imports: [
    CommonModule,
    NotificacionesRoutingModule,
    MatCardModule,
    MatTableModule,
    MatDividerModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatNativeDateModule,
    MatTooltipModule,
    MatListModule
  ]
})
export class NotificacionesModule { }
