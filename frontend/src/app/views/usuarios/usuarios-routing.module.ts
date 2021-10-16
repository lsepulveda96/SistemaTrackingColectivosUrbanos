import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UsuarioEditComponent } from './usuario-edit/usuario-edit.component';
import { UsuarioViewComponent } from './usuario-view/usuario-view.component';
import { UsuariosListComponent } from './usuarios-list/usuarios-list.component';
import { UsuariosComponent } from './usuarios.component';

const routes: Routes = [ 
  { 
    path: '', 
    component: UsuariosComponent,
    children: [ 
      { path: '', component: UsuariosListComponent },
      { path: 'list', component: UsuariosListComponent },
      { path: 'new', component: UsuarioEditComponent },
      { path: 'edit/:id', component: UsuarioEditComponent },
      { path: 'view/:id', component: UsuarioViewComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsuariosRoutingModule { }
