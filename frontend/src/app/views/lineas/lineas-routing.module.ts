import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LineaEditComponent } from './linea-edit/linea-edit.component';
import { LineaViewComponent } from './linea-view/linea-view.component';
import { LineasListComponent } from './lineas-list/lineas-list.component';
import { LineasComponent } from './lineas.component';

const routes: Routes = [
  { 
    path: '', 
    component: LineasComponent,
    children: [
      { path: '', component: LineasListComponent },
      { path:'list', component: LineasListComponent },
      { path:'new', component: LineaEditComponent },
      { path:'edit/:id', component: LineaEditComponent },
      { path:'view/:id', component: LineaViewComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LineasRoutingModule { }
