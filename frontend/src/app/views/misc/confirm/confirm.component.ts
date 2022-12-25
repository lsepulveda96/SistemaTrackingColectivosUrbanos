import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-confirm',
  templateUrl: './confirm.component.html',
  styleUrls: ['./confirm.component.css']
})
export class ConfirmComponent implements OnInit {

  titulo: string;
  mensaje: string;

  constructor(
    public dialogref: MatDialogRef<ConfirmComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  ngOnInit(): void {
    this.titulo = this.data.titulo;
    this.mensaje = this.data.mensaje;
  }

  onAceptar() {
    this.dialogref.close( true );
  }

  onCancelar() {
    this.dialogref.close( false );
  }
}
