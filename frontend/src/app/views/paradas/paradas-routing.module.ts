import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ParadaEditComponent } from './parada-edit/parada-edit.component';
import { ParadaViewComponent } from './parada-view/parada-view.component';
import { ParadasListComponent } from './paradas-list/paradas-list.component';
import { ParadasComponent } from './paradas.component';
import { AccessRoutesGuard } from 'src/app/helpers/access-routes.guard';

const routes: Routes = [
  { 
    path: '', 
    component: ParadasComponent,
    children: [
      { path: '', component: ParadasListComponent },
      { path: 'list', component: ParadasListComponent },
      { path: 'new', canActivate: [AccessRoutesGuard] ,component: ParadaEditComponent },
      { path: 'edit/:codigo', canActivate: [AccessRoutesGuard],component: ParadaEditComponent },
      { path: 'view/:codigo', component: ParadaViewComponent },
    ] 
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ParadasRoutingModule { }
