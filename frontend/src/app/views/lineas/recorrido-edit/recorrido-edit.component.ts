import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Linea } from 'src/app/data/linea';
import { Recorrido } from 'src/app/data/recorrido';
import { LineaService } from 'src/app/services/linea.service';
import { ParadaService } from 'src/app/services/parada.service';
import { MessageService } from 'src/app/services/message.service';
import { ESRI_PARAMS } from 'src/app/services/esriConfig';

import * as L from 'leaflet';
import 'leaflet-routing-machine';
import 'esri-leaflet-geocoder/dist/esri-leaflet-geocoder.css';
import 'esri-leaflet-geocoder';
import * as ELG from 'esri-leaflet-geocoder';

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

  paradaInicial: any;
  paradasRecorrido: any[] = [];
  trayectos: any[] = [];
  waypoints: any[] = [];

  map: L.Map;
  actualCoord: any;
  ultimaCoord: any;

  iconParada = L.icon({
    iconUrl: 'assets/images/stopbus.png',
    iconSize: [45, 50],
    iconAnchor: [45, 50],
    popupAnchor: [-8, -37]
  });

  iconParadaIn = L.icon({
    iconUrl: 'assets/images/stopbusin.png',
    iconSize: [45, 50],
    iconAnchor: [45, 50],
    popupAnchor: [-8, -37]
  });

  iconDiv = L.divIcon({
    html: '<i class="bi bi-geo-fill" style="font-size: 30px; color:black"></i>',
    iconSize: [35, 40],
    iconAnchor: [40, 45],
    popupAnchor: [-15, -30],
    className: 'myDivIcon'
  });
  iconDivIn = L.divIcon({
    html: '<i class="bi bi-geo-fill" style="font-size: 30px; color:blue"></i>',
    iconSize: [35, 40],
    iconAnchor: [40, 45],
    popupAnchor: [-15, -30],
    className: 'myDivIcon'
  });

  pInicialIC = new FormControl(null);

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
    this.paradasRecorrido = [];
    this.waypoints = [];
    this.trayectos = [];
    this.waiting = true;
    this.servicioParada.getParadasActivas().subscribe(result => {
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

  seleccionInicial() {
    this.clearParadas().
      then(result => {
        this.loadParadasToMap();
        
      },
        error => {
          console.log("Error eliminando paradas")
        });
  }

  async clearParadas() {
    this.map.eachLayer((layer: any) => {
      if (layer._url != 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png')
        this.map.removeLayer(layer);
    });
  }

  guardarRecorrido() {

  }

  actualizarRecorrido() {

  }

  showMap() {

  }

  control: any;

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

    this.map.on('click', (ev: any) => {
      if (!this.pInicialIC.value) {
        this._msg.showMessage('Debe seleccionar una parada de inicio para comenzar a marcar el recorrido', 'ERROR');
        return;
      }
      if (this.trayectos.length == 0) {
        const puntoInicio = new L.LatLng(this.pInicialIC.value.coordenada.lat, this.pInicialIC.value.coordenada.lng);
        const puntoFin = new L.LatLng(ev.latlng.lat, ev.latlng.lng);
      }

      //var geocodeService = L.esri.Geocoding.geocodeService();
      const geocodeService = ELG.geocodeService({
        apiKey: ESRI_PARAMS.apiKey,
        token: ESRI_PARAMS.token
      });
      console.log("map click point original: ", ev.latlng);
      /*       geocodeService.reverse().latlng(ev.latlng).run(function (error, result) { */
      geocodeService.reverse().latlng(ev.latlng).run((error:any, result:any) => {
        if (error) {
          console.log("geocode service reverse error: ", error);
          return;
        }
        console.log("geocode service reverse result: ", result);
        const pointAdjust = result.latlng;
        console.log("map click point adjust: ", pointAdjust);

        console.log("pinicial value: ", this.pInicialIC.value);
        const pinicial = new L.LatLng( this.pInicialIC.value.coordenada.lat, this.pInicialIC.value.coordenada.lng );
        console.log("pinicial: ", pinicial );
        //this.control = ELG.Routing.control({
        this.control = L.Routing.control({
          waypoints: [pinicial, pointAdjust],
          plan: L.Routing.plan([pinicial, pointAdjust], {
            createMarker: function (i, wp) {
              const marker = L.marker(wp.latLng, { icon: this.iconDiv, draggable: true });
              return marker;
            }
          }),
          showAlternatives: false,
          // language: 'es',
          autoRoute: true
        }).addTo(this.map);
      });

    });
  }

  loadParadasToMap() {
    for (let parada of this.paradas) {
      let icon = this.iconDiv;
      if ((this.pInicialIC.value && this.pInicialIC.value.codigo == parada.codigo) ||
        this.paradasRecorrido.find(par => par.codigo == parada.codigo))
        icon = this.iconDivIn;

      const marker = L.marker([parada.coordenada.lat, parada.coordenada.lng], { icon: icon });
      marker.bindPopup(parada.codigo + ':' + parada.direccion,
        { closeOnClick: false, autoClose: false });
      marker.addTo(this.map);

      if (!this.pInicialIC.value) // si no hay parada inicial se muestra el popup de todas las paradas.
        marker.openPopup();
      else if (this.pInicialIC.value.codigo == parada.codigo) // Si hay parada inicial solo se muestra el popup de esta.
        marker.openPopup();
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
