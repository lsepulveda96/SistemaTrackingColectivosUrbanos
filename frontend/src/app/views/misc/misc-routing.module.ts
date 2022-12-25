import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ConfirmComponent } from './confirm/confirm.component';
import { MiscComponent } from './misc.component';

const routes: Routes = [
  { 
    path: '', 
    component: MiscComponent,
    children: [ 
      { path:'confirm', component: ConfirmComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MiscRoutingModule { }
