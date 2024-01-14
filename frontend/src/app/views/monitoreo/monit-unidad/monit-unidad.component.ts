import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { MonitorService } from 'src/app/services/monitor.service';
import { ColectivoRecorrido } from 'src/app/data/colectivoRecorrido';
import * as L from 'leaflet';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Coordenada } from 'src/app/data/coordenada';
import { Observable, Subscription } from 'rxjs';

@Component({
  selector: 'app-monit-unidad',
  templateUrl: './monit-unidad.component.html',
  styleUrls: ['./monit-unidad.component.css']
})
export class MonitUnidadComponent implements OnInit, OnDestroy {

  waiting: boolean;
  colectivoRecorrido: ColectivoRecorrido;
  ultimaCoordenada: Coordenada;
  id: number;
  map: any;
  marker: any;
  iconParada: any = L.icon({
    iconUrl: 'assets/images/bus-stop-pin.png',
    iconSize: [45, 50],
    iconAnchor: [45, 50],
    popupAnchor: [-25, -45]
  });

  time = new Observable<string>(observer => {
    setInterval(() => observer.next(new Date().toString()), 1000);
  });

  coordenadaSubs: Subscription;
  trace: Coordenada[];
  recGroup: any; // grupo de trayectos del recorrido actual seleccionado.
  coordenadasGroup: any; // grupo de coordenadas leidas;
  colorRec = '#ffa500';

  constructor(
    private serviceMonitor: MonitorService,
    private route: ActivatedRoute,
    private _snackbar: MatSnackBar
  ) { }


  ngOnInit(): void {
    this.initMap();
    const id = this.route.snapshot.paramMap.get('id');
    this.getColectivoEnTransito(parseInt(id));
    this.getCoordenadasLoop(parseInt(id));
    //this.getCoordenadasColectivoEnTransito(parseInt(id));
  }

  ngOnDestroy(): void {
    console.log("On destroy")
    this.serviceMonitor.stopTimerCoordenadas();
  }

  getColectivoEnTransito(idtransito: number) {
    this.waiting = true;
    this.serviceMonitor.getUnidadRecorridoTransito(idtransito)
      .subscribe(result => {
        this.waiting = false;
        this.colectivoRecorrido = result.data;
      });
  }

  private initMap() {
    this.map = L.map('map', {
      center: [-42.775935, -65.038144],
      zoom: 17
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      minZoom: 12,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(this.map);
  }

  getCoordenadasLoop(idtransito: number) {
    this.trace = [];
    this.coordenadaSubs = this.serviceMonitor.startTimerCoordenadas(idtransito).subscribe(data => {
      console.log("ultima coordenada: ", data);
      this.ultimaCoordenada = data;
      if (this.ultimaCoordenada) {
        this.trace.push(this.ultimaCoordenada);
        this.showPosicion();
        this.showRecorrido();
      }
    });
  }

  showPosicion() {
    if (this.marker)
      this.map.removeLayer(this.marker);
    this.marker = L.marker([this.ultimaCoordenada.lat, this.ultimaCoordenada.lng], { icon: this.iconParada, draggable: false })
      .addTo(this.map)
      .bindPopup('Unidad: ' + this.colectivoRecorrido.colectivo.unidad.toString())
      .openPopup();
    this.map.panTo(new L.LatLng(this.ultimaCoordenada.lat, this.ultimaCoordenada.lng));
  }

  showRecorrido() {
    if (this.coordenadasGroup)
      this.coordenadasGroup.clearLayers();
    else
      this.coordenadasGroup = new L.LayerGroup();

    if (this.recGroup) // elimina todos los layers en el grupo
      this.recGroup.clearLayers();
    else // si el layer group no esta creado se crea.
      this.recGroup = new L.LayerGroup();

    // Crea el polyline que mostrara el recorrido.    
    const polyline = L.polyline([], { color: this.colorRec });

    // Toma los trayectos del recorrido y en base a ellos genera el polyline
    const trays = this.trace.map((coord: any) => new L.LatLng(coord.lat, coord.lng));
    for (let tray of trays) {
      polyline.addLatLng(tray);
    }

    this.recGroup.addLayer(polyline);
    this.recGroup.addTo(this.map);
    //this.map.fitBounds(trays);
  }

  getCoordenadasColectivoEnTransito(idtransito: number) {
    this.waiting = true;
    this.serviceMonitor.getCoordenadasColectivoRecorrido(idtransito)
      .subscribe(result => {
        this.waiting = false;
        console.log("get coordenada colectivo transito: ", result);
        if (result.error) {
          this._snackbar.open(result.mensaje, '', {
            duration: 4500,
            verticalPosition: 'bottom', // 'top' | 'bottom'
            horizontalPosition: 'end', //'start' | 'center' | 'end' | 'left' | 'right'
            panelClass: ['red-snackbar'],
          });
          //this.router.navigate(['../..'], { relativeTo: this.route });
        }
        else {
          /* this.coordenadaColeRec = result.data;
          this.marker = L.marker([this.coordenadaColeRec.lat, this.coordenadaColeRec.lng], { icon: this.iconParada, draggable: false })
            .addTo(this.map)
            .openPopup(); */
        }
      });
  }
}

