import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-parada-qr',
  templateUrl: './parada-qr.component.html',
  styleUrls: ['./parada-qr.component.css']
})
export class ParadaQrComponent implements OnInit {

  waiting: boolean;
  linea: any;
  parada: any;
  recorrido: any;
  qrdata: any;
  width: string;
  iconImg = '../../../assets/images/stcuadmin_icon.png';

  constructor(
    public dialogRef: MatDialogRef<ParadaQrComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) { }

  ngOnInit(): void {
    this.linea = this.data.linea
    this.parada = this.data.parada;
    this.recorrido = this.data.recorrido;

    this.width = this.dialogRef.componentInstance.width;

    /*this.qrdata = JSON.stringify({
      linea: this.linea.id,
      parada: this.parada.codigo,
      par: this.parada.direccion,
      recorrido: this.recorrido.id,
      rec2: this.recorrido.denominacion,
    });*/

    this.qrdata = JSON.stringify({
      lineaId: this.linea.id,
      linea: this.linea.denominacion,
      paradaId: this.parada.codigo,
      direccion: this.parada.direccion,
      recorridoId: this.recorrido.id,
      recoridoDenom: this.recorrido.denominacion
    });
    this.qrdata = this.qrdata.slice(1,-1); // para eliminar corchetes al inicio y al final 
  }

  cerrar() {
    this.dialogRef.close();
  }

  saveAsImage(parent: any) {
    this.waiting = true;
      // fetches base 64 data from canvas
    const parentElement = parent.qrcElement.nativeElement
        .querySelector("canvas")
        .toDataURL("image/png")

    if (parentElement) {
      // converts base 64 encoded image to blobData
      let blobData = this.convertBase64ToBlob(parentElement)
      // saves as image
      const blob = new Blob([blobData], { type: "image/png" })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement("a")
      link.href = url
      // name of the file
      link.download = this.linea.denominacion.replace(' ','') + '_parada' + this.parada.codigo;
      link.click()
    }
    this.waiting = false;
  }

  private convertBase64ToBlob(Base64Image: string) {
    // split into two parts
    const parts = Base64Image.split(";base64,")
    // hold the content type
    const imageType = parts[0].split(":")[1]
    // decode base64 string
    const decodedData = window.atob(parts[1])
    // create unit8array of size same as row data length
    const uInt8Array = new Uint8Array(decodedData.length)
    // insert all character code into uint8array
    for (let i = 0; i < decodedData.length; ++i) {
      uInt8Array[i] = decodedData.charCodeAt(i)
    }
    // return blob image after conversion
    return new Blob([uInt8Array], { type: imageType })
  }

}
