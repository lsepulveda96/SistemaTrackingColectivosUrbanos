import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ParadasRoutingModule } from './paradas-routing.module';
import { ParadasComponent } from './paradas.component';
import { ParadaEditComponent } from './parada-edit/parada-edit.component';
import { ParadaViewComponent } from './parada-view/parada-view.component';
import { ParadasListComponent } from './paradas-list/paradas-list.component';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatTooltipModule } from '@angular/material/tooltip';


@NgModule({
  declarations: [
    ParadasComponent,
    ParadaEditComponent,
    ParadaViewComponent,
    ParadasListComponent
  ],
  imports: [
    CommonModule,
    ParadasRoutingModule,
    ReactiveFormsModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatTableModule,
    MatIconModule,
    MatDividerModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatTooltipModule
  ]
})
export class ParadasModule { }
