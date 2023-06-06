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
import { RecorridoViewComponent } from './recorrido-view/recorrido-view.component';
import { RecorridosListComponent } from './recorridos-list/recorridos-list.component';
import { RecorridoEditComponent } from './recorrido-edit/recorrido-edit.component';
import { ParadasEditComponent } from './paradas-edit/paradas-edit.component';
import { ParadasViewComponent } from './paradas-view/paradas-view.component';
import { MatListModule } from '@angular/material/list';
import { MatSelectModule } from '@angular/material/select';
import { ScrollingModule } from '@angular/cdk/scrolling';


@NgModule({
  declarations: [
    LineasComponent,
    LineasListComponent,
    LineaViewComponent,
    LineaEditComponent,
    RecorridoViewComponent,
    RecorridosListComponent,
    RecorridoEditComponent,
    ParadasEditComponent,
    ParadasViewComponent
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
    MatTooltipModule,
    MatListModule,
    MatSelectModule,
    ScrollingModule
  ]
})
export class LineasModule { }
