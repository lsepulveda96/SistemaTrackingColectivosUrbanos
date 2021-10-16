import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MonitoreoRoutingModule } from './monitoreo-routing.module';
import { MonitoreoComponent } from './monitoreo.component';


@NgModule({
  declarations: [
    MonitoreoComponent
  ],
  imports: [
    CommonModule,
    MonitoreoRoutingModule
  ]
})
export class MonitoreoModule { }
