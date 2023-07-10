import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MonitoreoComponent } from './monitoreo.component';
import { EnrecorridoListComponent } from './enrecorrido-list/enrecorrido-list.component';

const routes: Routes = [{ 
  path: '', component: MonitoreoComponent,
  children: [
    { path:'', component: EnrecorridoListComponent },
    { path:'list', component: EnrecorridoListComponent }
  ]
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MonitoreoRoutingModule { }
