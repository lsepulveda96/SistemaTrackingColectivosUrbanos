import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ColectivosRoutingModule } from './colectivos-routing.module';
import { ColectivosComponent } from './colectivos.component';
import { ColectivosListComponent } from './colectivos-list/colectivos-list.component';
import { ColectivoEditComponent } from './colectivo-edit/colectivo-edit.component';
import { ColectivoViewComponent } from './colectivo-view/colectivo-view.component';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatInputModule } from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatTooltipModule } from '@angular/material/tooltip';


@NgModule({
  declarations: [
    ColectivosComponent,
    ColectivosListComponent,
    ColectivoEditComponent,
    ColectivoViewComponent
  ],
  imports: [
    CommonModule,
    ColectivosRoutingModule,
    ReactiveFormsModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatTableModule,
    MatIconModule,
    MatDividerModule,
    MatInputModule,
    MatFormFieldModule,
    MatGridListModule,
    MatButtonModule,
    MatSnackBarModule,
    MatDatepickerModule,
    MatTooltipModule
  ]
})
export class ColectivosModule { }
