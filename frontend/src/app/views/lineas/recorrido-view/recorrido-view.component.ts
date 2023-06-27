import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Linea } from 'src/app/data/linea';
import { LineaService } from 'src/app/services/linea.service';

import * as L from 'leaflet';
import { MessageService } from 'src/app/services/message.service';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-recorrido-view',
  templateUrl: './recorrido-view.component.html',
  styleUrls: ['./recorrido-view.component.css']
})
export class RecorridoViewComponent implements OnInit {

  waiting: boolean;
  id: any;
  linea: Linea;
  recorridos: any[]; // recorridos activos
  map: any;
  recGroup: any; // grupo de trayectos del recorrido actual seleccionado.
  paradasGroup: any; // grupo de paradas del recorrido actual seleccionado.
  recorridoIC = new FormControl(null);
  viewParadas: boolean = false;
  showParadasIC = new FormControl(false);

  colors = ['#008000', '#0000ff', '#ffa500', '#a52a2a', '#8b008b', '#1e90ff'];

  constructor(
    private serviceLinea: LineaService,
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
      if (!result.error) {
        this.recorridos = result.data.map((rec: any, i: number) => {
          rec.color = this.colors[i];
          return rec;
        });
      }
    });
    setTimeout(() => {
      this.inicializarMapa();
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

  loadRecorridoMapa(recorrido: any) {
    const trays = recorrido.trayectos.map((wp: any) => new L.LatLng(wp.lat, wp.lng));

    const polyline = L.polyline([], { color: recorrido.color });
    polyline.addTo(this.map);

    for (let tray of trays) {
      polyline.addLatLng(tray);
      polyline.redraw();
    }
  }

  onSelectRecorrido() {
    this.showParadasIC.setValue(false);
    if (this.paradasGroup)
      this.paradasGroup.clearLayers();

    // Toma el recorrido seleccionado
    const recSel = this.recorridoIC.value[0];
    if (this.recGroup) // elimina todos los layers en el grupo
      this.recGroup.clearLayers();
    else // si el layer group no esta creado se crea.
      this.recGroup = new L.LayerGroup();

    // Crea el polyline que mostrara el recorrido.    
    const polyline = L.polyline([], { color: recSel.color });
    // Toma los trayectos del recorrido y en base a ellos genera el polyline
    const trays = recSel.trayectos.map((wp: any) => new L.LatLng(wp.lat, wp.lng));
    for (let tray of trays) {
      polyline.addLatLng(tray);
      //polyline.redraw();
    }
    this.recGroup.addLayer(polyline);
    this.recGroup.addTo(this.map);
  }

  changeShowParadas(event: any) {
    if (this.showParadasIC.value && this.recorridoIC.value) {
      const recSel = this.recorridoIC.value[0];
      if (!recSel.paradas) {
        this.waiting = true;
        this.serviceLinea.getParadasRecorrido( recSel.id ).subscribe( result => {
          this.waiting = false;
          if (!result.error) {
            recSel.paradas = result.data;
            this.showParadasRecorrido( recSel.paradas );
          }
        });
      }
      else {
        this.showParadasRecorrido( recSel.paradas );
      }
    }
    else if (!this.showParadasIC.value && this.paradasGroup) {
      this.paradasGroup.clearLayers();
    }
  }

  showParadasRecorrido( paradasRec: any[] ) {
    if (this.paradasGroup) 
      this.paradasGroup.clearLayers();
    else
      this.paradasGroup = new L.LayerGroup();

    for (let paradaRec of paradasRec ) {
      const mark = new L.Marker( L.latLng( paradaRec.parada.coordenada.lat, paradaRec.parada.coordenada.lng ) );
      const begin_end = paradaRec.orden == 0 ? 'INICIO ' : (paradaRec.orden == paradasRec.length-1 ? 'FIN ': ''); 
      mark.bindPopup(begin_end + paradaRec.parada.codigo + ': ' + paradaRec.parada.direccion,
        { closeOnClick: true, autoClose: true });
      this.paradasGroup.addLayer( mark );
    }
    this.paradasGroup.addTo(this.map);
  }

  nuevoRecorrido() {
    this.router.navigate(['../../new', this.linea.id], { relativeTo: this.route });
  }

  editarRecorrido(rec: any) {
    console.log("edit recorrido: ", rec);
  }
}
