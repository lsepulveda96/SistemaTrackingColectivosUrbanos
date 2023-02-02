import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(private _snackbar: MatSnackBar) { }

  showMessage( message: string, tipo: string ) {
    this._snackbar.open( message, tipo=='ERROR' ? 'Error':'Exito', {
      duration: 4500,
      verticalPosition: 'bottom',
      horizontalPosition: 'end',
      panelClass: tipo=='ERROR' ? ['red-snackbar']:['blue-snackbar']
    });
  }
}
