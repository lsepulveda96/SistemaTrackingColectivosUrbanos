import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PerfilRoutingModule } from './perfil-routing.module';
import { PerfilEditComponent } from './perfil-edit/perfil-edit.component';
import { PerfilComponent } from './perfil.component';


@NgModule({
  declarations: [
    PerfilComponent,
    PerfilEditComponent
  ],
  imports: [
    CommonModule,
    PerfilRoutingModule
  ]
})
export class PerfilModule { }
