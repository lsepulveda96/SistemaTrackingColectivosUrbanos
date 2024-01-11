import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MonitoreoComponent } from './monitoreo.component';
import { EnrecorridoListComponent } from './enrecorrido-list/enrecorrido-list.component';
import { MonitUnidadComponent } from './monit-unidad/monit-unidad.component';

const routes: Routes = [{ 
  path: '', component: MonitoreoComponent,
  children: [
    { path:'', component: EnrecorridoListComponent },
    { path:'list', component: EnrecorridoListComponent },
    { path: 'monit/:id', component: MonitUnidadComponent }
  ]
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MonitoreoRoutingModule { }
