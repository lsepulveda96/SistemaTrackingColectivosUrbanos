import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ColectivoEditComponent } from './colectivo-edit/colectivo-edit.component';
import { ColectivoViewComponent } from './colectivo-view/colectivo-view.component';
import { ColectivosListComponent } from './colectivos-list/colectivos-list.component';
import { ColectivosComponent } from './colectivos.component';
import { AccessRoutesGuard } from 'src/app/helpers/access-routes.guard';

const routes: Routes = [
  { 
    path: '', 
    component: ColectivosComponent,
    children: [ 
      { path:'', component: ColectivosListComponent },
      { path:'list', component: ColectivosListComponent },
      { path: 'new', canActivate: [AccessRoutesGuard],component: ColectivoEditComponent },
      { path: 'edit/:id', canActivate: [AccessRoutesGuard], component: ColectivoEditComponent },
      { path: 'view/:id', component: ColectivoViewComponent }
    ]
  }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ColectivosRoutingModule { }
