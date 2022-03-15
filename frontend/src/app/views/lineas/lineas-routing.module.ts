import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ParadasListComponent } from '../paradas/paradas-list/paradas-list.component';
import { LineaEditComponent } from './linea-edit/linea-edit.component';
import { LineaViewComponent } from './linea-view/linea-view.component';
import { LineasListComponent } from './lineas-list/lineas-list.component';
import { LineasComponent } from './lineas.component';
import { ParadasEditComponent } from './paradas-edit/paradas-edit.component';
import { ParadasViewComponent } from './paradas-view/paradas-view.component';
import { RecorridoEditComponent } from './recorrido-edit/recorrido-edit.component';
import { RecorridoViewComponent } from './recorrido-view/recorrido-view.component';
import { RecorridosListComponent } from './recorridos-list/recorridos-list.component';

const routes: Routes = [
  { 
    path: '', 
    component: LineasComponent,
    children: [
      { path: '', component: LineasListComponent },
      { path:'list', component: LineasListComponent },
      { path:'new', component: LineaEditComponent },
      { path:'edit/:id', component: LineaEditComponent },
      { path:'view/:id', component: LineaViewComponent },
      { path:'recorrido/view/:id', component: RecorridoViewComponent },
      { path:'recorrido/:modo/:id', component: RecorridoEditComponent },
      { path:'recorridos/:id', component: RecorridosListComponent },
      { path:'paradas/:id', component: ParadasListComponent },
      { path:'paradas/view/:id', component: ParadasViewComponent },
      { path:'paradas/edit/:id', component: ParadasEditComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LineasRoutingModule { }
