import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MiscRoutingModule } from './misc-routing.module';
import { MiscComponent } from './misc.component';
import { ConfirmComponent } from './confirm/confirm.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';


@NgModule({
  declarations: [
    MiscComponent,
    ConfirmComponent,
  ],
  imports: [
    CommonModule,
    MiscRoutingModule,
    MatDialogModule,
    MatCardModule,
    MatButtonModule
  ]
})
export class MiscModule { }
