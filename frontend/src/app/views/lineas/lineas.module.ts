import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LineasRoutingModule } from './lineas-routing.module';
import { LineasComponent } from './lineas.component';
import { LineasListComponent } from './lineas-list/lineas-list.component';
import { LineaViewComponent } from './linea-view/linea-view.component';
import { LineaEditComponent } from './linea-edit/linea-edit.component';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatTooltipModule } from '@angular/material/tooltip';


@NgModule({
  declarations: [
    LineasComponent,
    LineasListComponent,
    LineaViewComponent,
    LineaEditComponent
  ],
  imports: [
    CommonModule,
    LineasRoutingModule,
    ReactiveFormsModule,
    MatCardModule,
    MatDividerModule,
    MatProgressSpinnerModule,
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatInputModule,
    MatTooltipModule
  ]
})
export class LineasModule { }
