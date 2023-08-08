import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MiscRoutingModule } from './misc-routing.module';
import { MiscComponent } from './misc.component';
import { ConfirmComponent } from './confirm/confirm.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { NoAuthorizedComponent } from './no-authorized/no-authorized.component';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';


@NgModule({
  declarations: [
    MiscComponent,
    ConfirmComponent,
    NoAuthorizedComponent,
  ],
  imports: [
    CommonModule,
    MiscRoutingModule,
    MatDialogModule,
    MatCardModule,
    MatButtonModule,
    MatDividerModule,
    MatIconModule
  ]
})
export class MiscModule { }
