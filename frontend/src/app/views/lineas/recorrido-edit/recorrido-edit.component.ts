import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Linea } from 'src/app/data/linea';
import { Recorrido } from 'src/app/data/recorrido';
import { LineaService } from 'src/app/services/linea.service';
import { ParadaService } from 'src/app/services/parada.service';
import { MessageService } from 'src/app/services/message.service';

import * as L from 'leaflet';
import 'leaflet-routing-machine';
import 'esri-leaflet-geocoder/dist/esri-leaflet-geocoder.css';
import 'esri-leaflet-geocoder';

import { Parada } from 'src/app/data/parada';
import { E } from '@angular/cdk/keycodes';
import { last } from 'rxjs-compat/operator/last';

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
  trayectos: any[] = [];
  waypoints: any[] = [];

  map: L.Map; // Mapa en pantalla.
  actualCoord: any; // coordenada actual.
  ultimaCoord: any; // ultima coordenada.

  paradasDisponibles: Parada[]; // lista de paradas disponibles para agregar al recorrido.
  paradasRecorrido: Parada[]; // lista de paradas en el recorrido.
  paradaIC = new FormControl(null); // Parada seleccionada de la lista de disponibles.

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

  /**
   * Configura para generar nuevo recorrido y habilita edicion.
   */
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
        this.paradasDisponibles = result.data;
        this.loadParadasToMap();
      }
    });
  }

  /**
   * Carga el recorrido y lo habilita para edicion.
   * @param id 
   */
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

  control: any;

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

  /**
   * Carga todas las paradas habilitadas en el mapa.
   */
  loadParadasToMap() {
    const ultimaParada = this.paradasRecorrido[this.paradasRecorrido.length - 1];
    this.paradas.forEach((parada: any, index: number) => {
      let paradaIn = false;
      if (this.paradasRecorrido && this.paradasRecorrido.find(par => par.codigo == parada.codigo))
        paradaIn = true;

      const icon = paradaIn ? this.iconDivIn : this.iconDiv;
      const marker = L.marker([parada.coordenada.lat, parada.coordenada.lng], { icon: icon });
      marker.bindPopup(parada.codigo + ': ' + parada.direccion,
        { closeOnClick: false, autoClose: false });
      marker.addTo(this.map);

      // Se muestran popup solo si no hay paradas en recorrido o si es la ultima parada del recorrido.
      if (!this.paradasRecorrido || this.paradasRecorrido.length == 0 || (ultimaParada && ultimaParada.codigo == parada.codigo))
        marker.openPopup();
    });
  }

  /**
   * Agrega una parada al recorrido y el mapa, y la quita de las paradas disponibles.
   * @returns 
   */
  agregarParada() {
    if (!this.paradaIC.value) // Si no se seleccion parada no hace nada.
      return;
    if (!this.paradasRecorrido) // Si el arreglo de parada es null se inicializa.
      this.paradasRecorrido = [];
    // Se agrega la parada al arreglo de paradas.
    this.paradasRecorrido.push(this.paradaIC.value);
    // se elimina la parada de la lista de paradas disponibles
    this.paradasDisponibles = this.paradasDisponibles.filter(par => par.codigo != this.paradaIC.value.codigo);

    this.paradaIC.setValue(null);
    this.markParadaToMap();
  }

  /**
   * Marca la ultima parada en el mapa como una parada del recorrido, y traza un recorrido automatico entre las dos ultimas. 
   */
  markParadaToMap() {
    this.clearParadas().
      then(result => {
        this.loadParadasToMap();
        this.traceRecorridoUltimasParadas();
      }, error => {
        console.log("Error eliminando paradas")
      });
  }

  /**
   * Elimima todos los marcadores de paradas
   */
  async clearParadas() {
    this.map.eachLayer((layer: any) => {
      if (layer._url != 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png')
        this.map.removeLayer(layer);
    });
  }

  /**
   * Elimina la ultima parada del recorrido de la lista y del mapa.
   */
  quitarUltimaParada() {
    const len = this.paradasRecorrido.length;
    if (this.control && len > 0) {
      if (len == 1) {
        const paradaRemove = this.paradasRecorrido.pop();
        this.paradasDisponibles.push(paradaRemove);
        console.log("paradas recorrido: ", this.paradasRecorrido );
        console.log("paradas disponibles: ", this.paradasDisponibles );
        //this.control.setWaypoints([]);
      }
      else { // if (len > 2) 
        const wps = this.control.getWaypoints();
        const anteultimaParada = this.paradasRecorrido[len - 2];
        console.log("Coordenada ultima parada: ", anteultimaParada);
        let lastwp = wps.pop();
        do {
          if (lastwp.latLng.lat != anteultimaParada.coordenada.lat && lastwp.latLng.lng != anteultimaParada.coordenada.lng) {
            lastwp = wps.pop();
            console.log("remove waypoint: ", lastwp);
          }
          else {
            wps.push(lastwp);
            lastwp = null;
          }
        }
        while (lastwp != null);
        const paradaRemove = this.paradasRecorrido.pop();
        this.paradasDisponibles.push(paradaRemove);
        this.control.setWaypoints(wps);
      }
      console.log("waypoints after remove: ", this.control.getWaypoints());
    }
  }

  /**
   * Construye recorrido automatico entre las ultimas dos paradas del recoorido.
   */
  traceRecorridoUltimasParadas() {
    const len = this.paradasRecorrido.length;
    if (!this.control) {
      if (len >= 2) {
        const ultimaParada = this.paradasRecorrido[len - 1];
        const anteultimaParada = this.paradasRecorrido[len - 2];
        const ultima = new L.LatLng(ultimaParada.coordenada.lat, ultimaParada.coordenada.lng);
        const anteultima = new L.LatLng(anteultimaParada.coordenada.lat, anteultimaParada.coordenada.lng);
        this.control = L.Routing.control({
          waypoints: [anteultima, ultima],
          show: false,
          autoRoute: true,
          plan: L.Routing.plan([anteultima, ultima], {
            addWaypoints: true, draggableWaypoints: true,
            createMarker: (i, wp, n) => {
              let marker: L.Marker;
              if (i == 0 || i == n - 1)
                marker = L.marker(wp.latLng, { icon: this.iconDivIn, draggable: false });
              return marker;
            }
          })
        }).addTo(this.map);
        // Oculta el itinerario
        const routingControlContainer = this.control.getContainer();
        const controlContainerParent = routingControlContainer.parentNode;
        controlContainerParent.removeChild(routingControlContainer);
        console.log("Waypoints after add: ", this.control.getWaypoints());
      }
    }
    else {
      if (len >= 1) {
        const ultimaParada = this.paradasRecorrido[len - 1];
        const ultima = new L.LatLng(ultimaParada.coordenada.lat, ultimaParada.coordenada.lng);
        const wps = this.control.getWaypoints();
        wps.push(ultima);
        this.control.setWaypoints(wps);
        this.control.addTo(this.map);
        // Oculta el itinerario
        const routingControlContainer = this.control.getContainer();
        const controlContainerParent = routingControlContainer.parentNode;
        controlContainerParent.removeChild(routingControlContainer);
        console.log("Waypoints after add: ", this.control.getWaypoints());
      }
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
