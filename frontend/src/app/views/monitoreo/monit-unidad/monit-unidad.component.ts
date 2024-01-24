import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MonitorService } from 'src/app/services/monitor.service';
import { ColectivoRecorrido } from 'src/app/data/colectivoRecorrido';
import * as L from 'leaflet';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Coordenada } from 'src/app/data/coordenada';
import { Subscription, timer } from 'rxjs';
import { map } from 'rxjs/operators';
import { LineaService } from 'src/app/services/linea.service';

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
    iconUrl: 'assets/images/stopbus.png',
    iconSize: [45, 50],
    iconAnchor: [45, 50],
    popupAnchor: [-8, -37]
  });

  iconBus: any = L.icon({
    iconUrl: 'assets/images/buspin.png',
    iconSize: [45, 50],
    iconAnchor: [45, 50],
    popupAnchor: [-25, -45]
  });


  coordenadaSubs: Subscription;
  trace: Coordenada[];
  recGroup: any; // grupo de trayectos del recorrido actual seleccionado.
  coordenadasGroup: any; // grupo de coordenadas leidas;
  colorRecBus = '#1e90ff';
  colorRec = '#008000';

  paradasGroup: any; // grupo de paradas del recorrido.

  constructor(
    private serviceMonitor: MonitorService,
    private serviceLinea: LineaService,
    private route: ActivatedRoute,
    private _snackbar: MatSnackBar
  ) { }


  ngOnInit(): void {
    this.initMap();
    const id = this.route.snapshot.paramMap.get('id');
    this.getColectivoEnTransito(parseInt(id));
    //this.getCoordenadasLoop(parseInt(id));
  }

  ngOnDestroy(): void {
    if (this.coordenadaSubs) {
      this.serviceMonitor.last = 9; // solo prueba
      this.coordenadaSubs.unsubscribe();
    }
  }

  getColectivoEnTransito(idtransito: number) {
    this.waiting = true;
    this.serviceMonitor.getUnidadRecorridoTransito(idtransito)
      .subscribe(result => {
        this.waiting = false;
        this.colectivoRecorrido = result.data;
        console.log("Colectivo recorrido en transito: ", this.colectivoRecorrido);
        this.getParadasRecorrido( this.colectivoRecorrido.recorridoId)
        this.initCoordenadas(this.colectivoRecorrido.coordenadas);
        this.getCoordenadasLoop(idtransito);
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

  // Inicializa el trayecto recorrido hasta el momente de inicio de monitoreo
  initCoordenadas(coordenadas: any[]) {
    this.trace = [];
    this.ultimaCoordenada = null;
    if (coordenadas) {
      for (let coord of coordenadas) {
        if (!this.equalCoordenada(coord)) {
          this.ultimaCoordenada = coord;
          if (this.ultimaCoordenada) {
            this.trace.push(this.ultimaCoordenada);
            this.showPosicion();
            this.showRecorrido();
          }
        }
      }
    }
  }

  // Comienza a recuperar coordenadas actualizadas cada 3 segundos
  getCoordenadasLoop(idtransito: number) {
    //this.trace = [];
    this.ultimaCoordenada = null;
    this.coordenadaSubs = timer(0, 3000).pipe(
      map(() => {
        this.serviceMonitor.getUltimaCoordenadaColectivoRecorrido(idtransito)
          .subscribe((result: any) => {
            if (result && !result.error && !this.equalCoordenada(result.data)) {
              this.ultimaCoordenada = result.data;
              if (this.ultimaCoordenada) {
                this.trace.push(this.ultimaCoordenada);
                this.showPosicion();
                this.showRecorrido();
              }
            }
            else
              console.log("No hay coordenada o es misma coordenada: ", this.ultimaCoordenada, ", ", result);
          });
      })).subscribe();
  }

  showPosicion() {
    if (this.marker)
      this.map.removeLayer(this.marker);
    this.marker = L.marker([this.ultimaCoordenada.lat, this.ultimaCoordenada.lng], { icon: this.iconBus, draggable: false })
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
    const polyline = L.polyline([], { color: this.colorRecBus, dashArray: [10,10] });

    // Toma los trayectos del recorrido y en base a ellos genera el polyline
    const trays = this.trace.map((coord: any) => new L.LatLng(coord.lat, coord.lng));
    for (let tray of trays) {
      polyline.addLatLng(tray);
    }

    this.recGroup.addLayer(polyline);
    this.recGroup.addTo(this.map);
    //this.map.fitBounds(trays);
  }

  getParadasRecorrido( id: number ) {
    this.serviceLinea.getParadasRecorrido( id ).subscribe( result => {
      if (!result.error) {
        const paradasRec = result.data.map( (pr:any) => pr.parada );
        console.log("paradas de recorrido " + id, paradasRec );
        this.showParadasRecorrido( paradasRec );
      }
    });
  }

  showParadasRecorrido( paradas: any[]) {
    if (this.paradasGroup)
      this.paradasGroup.clearLayers();
    else
      this.paradasGroup = new L.LayerGroup();
    
    // Toma los trayectos del recorrido y en base a ellos genera el polyline
    for (let parada of paradas) {
      const markParada = new L.Marker(L.latLng(parada.coordenada.lat, parada.coordenada.lng), {
        icon: this.iconParada
      });
      markParada.bindPopup( parada.codigo + ': ' + parada.direccion );
      this.paradasGroup.addLayer(markParada);
    }
    this.paradasGroup.addTo(this.map);
  }



  private equalCoordenada(newCoordenada: Coordenada): boolean {
    if (!this.ultimaCoordenada)
      return false;
    if (this.ultimaCoordenada.lat == newCoordenada.lat && this.ultimaCoordenada.lng == newCoordenada.lng)
      return true;
    return false;
  }
}

