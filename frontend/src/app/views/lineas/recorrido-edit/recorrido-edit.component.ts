import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Linea } from 'src/app/data/linea';
import { Recorrido } from 'src/app/data/recorrido';
import { LineaService } from 'src/app/services/linea.service';
import { MessageService } from 'src/app/services/message.service';
import { ESRI_PARAMS } from 'src/app/services/esriConfig';

import * as L from 'leaflet';
import * as ELG from 'esri-leaflet-geocoder';
import { ParadaService } from 'src/app/services/parada.service';



@Component({
  selector: 'app-recorrido-edit',
  templateUrl: './recorrido-edit.component.html',
  styleUrls: ['./recorrido-edit.component.css']
})
export class RecorridoEditComponent implements OnInit {

  modeNew: boolean;
  waiting: boolean;
  linea: Linea;
  recorrido: Recorrido;
  paradas: any[];

  map: L.Map;
  actualCoord: any;
  ultimaCoord: any;
  iconParada: any = L.icon({
    iconUrl: 'assets/images/stopbus.png',
    iconSize: [45, 50],
    iconAnchor: [45, 50],
    popupAnchor: [-8, -37]
  });

  constructor(
    private servicioLinea: LineaService,
    private servicioParada: ParadaService,
    private _msg: MessageService,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit(): void {
    const mod = this.route.snapshot.paramMap.get("modo");
    if (!mod) {
      this._msg.showMessage('No se indico modo (nuevo o edicion)', 'ERROR');
      this.router.navigate(['../..']);
    }
    this.modeNew = (mod == 'new');
    const id = this.route.snapshot.paramMap.get('id');

    this.waiting = true;
    this.servicioLinea.getLinea(parseInt(id)).subscribe(result => {
      this.waiting = false;
      this.linea = result.data;
    });

    this.inicializarMapa();
    if (this.modeNew)
      this.nuevoRecorrido();
    else
      this.editarRecorrido(parseInt(id));
  }

  nuevoRecorrido() {
    // cargar todas las paradas.
    this.waiting = true;
    this.servicioParada.getParadasActivas().subscribe( result => {
      this.waiting = false;
      if (!result.error) {
        this.paradas = result.data;
        this.loadParadasToMap();
      }
    });
  }

  editarRecorrido(id: number) {
    this.waiting = true;
    this.servicioLinea.getRecorrido(id).subscribe(result => {
      this.waiting = false;
      if (!result.error)
        this.recorrido = result.data;
    });
  }


  guardarRecorrido() {

  }

  actualizarRecorrido() {

  }

  showMap() {

  }

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

    this.map.on('click', (e) => {
      console.log("Click on map: ", e );
    });
  }

  loadParadasToMap() {
    for (let parada of this.paradas) {
      const marker = L.marker([parada.coordenada.lat, parada.coordenada.lng], { icon: this.iconParada })
      .addTo(this.map)
      .bindPopup(parada.direccion)
      .openPopup();
    }
  }

  getRecorrido() {

  }

  getParadasRecorrido() {

  }

  borrarUltimoTramo() {

  }

  borrarTodosTramos() {

  }

}
