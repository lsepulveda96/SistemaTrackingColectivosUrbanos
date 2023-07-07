import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';

import * as L from 'leaflet';
import { Linea } from 'src/app/data/linea';
import { Parada } from 'src/app/data/parada';
import { LineaService } from 'src/app/services/linea.service';
import { MessageService } from 'src/app/services/message.service';

@Component({
  selector: 'app-paradas-view',
  templateUrl: './paradas-view.component.html',
  styleUrls: ['./paradas-view.component.css']
})
export class ParadasViewComponent implements OnInit {

  waiting: boolean;
  linea: Linea;
  recorridos: any[];
  paradasRec: any[];

  constructor( private servicioLinea: LineaService, 
    private _msg: MessageService,
    private route: ActivatedRoute,
    private router: Router ) { }

  ngOnInit(): void {
    const idLinea = this.route.snapshot.paramMap.get('id');
    this.waiting = true;
    this.servicioLinea.getLinea( parseInt(idLinea ))
      .subscribe( result => {
        this.waiting = false;
        this.linea = result.data;
        console.log("Linea: ", this.linea );
      });
    this.servicioLinea.getRecorridosActivos( parseInt(idLinea))
      .subscribe( result => {
        console.log("Recorridos activos: ", result );
        this.recorridos = result.data;
      });
      
  }

  onSelectRecorrido(recorridoSelect: any) {
    console.log("selecciona recorrido: ", recorridoSelect );
    if (!recorridoSelect.paradas){
      this.waiting = true;
      this.servicioLinea.getParadasRecorrido( recorridoSelect.id ).subscribe( result => {
        this.waiting = false;
        console.log("Paradas de recorrido " + recorridoSelect.denominacion, ": ", result.data );
        this.paradasRec = result.data;
      });
    }
  }

  showQRCode( pr:any) {
    console.log("Imprimir QR Code: ", pr, " , linea: ", this.linea.id );
  }

}
