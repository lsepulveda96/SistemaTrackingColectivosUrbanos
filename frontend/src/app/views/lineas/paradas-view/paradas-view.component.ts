import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';

import { Linea } from 'src/app/data/linea';
import { LineaService } from 'src/app/services/linea.service';
import { MessageService } from 'src/app/services/message.service';
import { ParadaQrComponent } from '../parada-qr/parada-qr.component';

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
    private router: Router,
    private dialog: MatDialog ) { }

  ngOnInit(): void {
    const idLinea = this.route.snapshot.paramMap.get('id');
    this.waiting = true;
    this.servicioLinea.getLinea( parseInt(idLinea ))
      .subscribe( result => {
        this.waiting = false;
        this.linea = result.data;
      });
    this.servicioLinea.getRecorridosActivos( parseInt(idLinea))
      .subscribe( result => {
        this.recorridos = result.data;
      });
      
  }

  onSelectRecorrido(recorridoSelect: any) {
    if (!recorridoSelect.paradas){
      this.waiting = true;
      this.servicioLinea.getParadasRecorrido( recorridoSelect.id ).subscribe( result => {
        this.waiting = false;
        this.paradasRec = result.data;
      });
    }
  }

  showQRCode( pr:any, rec: any ) {
    const data = { 
      linea: this.linea, 
      parada: pr.parada, 
      recorrido: { id: rec.id, denominacion: rec.denominacion } 
    }
    this.dialog.open( ParadaQrComponent, { data: data });
  }

}
