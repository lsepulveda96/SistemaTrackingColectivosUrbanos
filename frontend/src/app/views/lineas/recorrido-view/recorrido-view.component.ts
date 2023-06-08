import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Linea } from 'src/app/data/linea';
import { Recorrido } from 'src/app/data/recorrido';
import { LineaService } from 'src/app/services/linea.service';

import * as L from 'leaflet';
import { MessageService } from 'src/app/services/message.service';

@Component({
  selector: 'app-recorrido-view',
  templateUrl: './recorrido-view.component.html',
  styleUrls: ['./recorrido-view.component.css']
})
export class RecorridoViewComponent implements OnInit {

  waiting: boolean;
  id: any;
  linea: Linea;
  recorridos: Recorrido[]; // recorridos activos
  map: any;

  constructor(private serviceLinea: LineaService,
    private _msg: MessageService,
    private router: Router,
    private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get("id");
    this.waiting = true;
    this.serviceLinea.getLinea(parseInt(this.id)).subscribe(result => {
      if (!result.error)
        this.linea = result.data;
    });
    this.serviceLinea.getRecorridosActivos(parseInt(this.id)).subscribe(result => {
      this.waiting = false;
      console.log("Recorrido activos de linea " + this.id, result );
      if (!result.error) {
        this.recorridos = result.data;
        this.inicializarMapa();
      }
    });
  }

  /**
   * Inicializa el view mapa.
   */
  inicializarMapa() {
    this.map = L.map('map', {
      center: [-42.775935, -65.038144],
      zoom: 14
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      minZoom: 12,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(this.map);
  }

  editarRecorrido() {
    console.log("Editar recorrido: ");
  }

  definirRecorrido() {
    console.log("Definir recorrido");
  }

  historialRecorridos() {
    console.log("Historial recorrido")
  }
}
