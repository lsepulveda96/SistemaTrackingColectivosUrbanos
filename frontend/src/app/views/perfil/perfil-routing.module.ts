import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PerfilComponent } from './perfil.component';
import { PerfilEditComponent } from './perfil-edit/perfil-edit.component';

const routes: Routes = [{ 
  path: '', 
  component: PerfilComponent,
  children: [
    { path:'', component: PerfilEditComponent }
  ]
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PerfilRoutingModule { }
