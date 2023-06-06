import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
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
import { CdkVirtualScrollViewport } from '@angular/cdk/scrolling';
import { filter, map, pairwise, throttleTime } from 'rxjs/operators';

@Component({
  selector: 'app-recorrido-edit',
  templateUrl: './recorrido-edit.component.html',
  styleUrls: ['./recorrido-edit.component.css']
})
export class RecorridoEditComponent implements OnInit {

  modeNew: boolean;
  waiting: boolean;
  linea: Linea; // Linea actual
  recorrido: Recorrido; // Recorrido actual de la linea.
  paradas: Parada[]; // lista de paradas activas.

  trayectos: any[] = []; // trayectos del recorrido.
  waypoints: any[] = []; // waypoints del recorrido.

  map: L.Map; // Mapa en pantalla.
  control: any; // Control de ruta en el mapa.

  paradasDisponibles: Parada[]; // lista de paradas disponibles para agregar al recorrido.
  paradasRecorrido: Parada[]; // lista de paradas en el recorrido.
  paradaIC = new FormControl(null); // Parada seleccionada de la lista de disponibles.
  denominacionIC = new FormControl('', Validators.required );

  // Icono de parada general.
  iconDiv = L.divIcon({
    html: '<i class="bi bi-geo-fill" style="font-size: 30px; color:black"></i>',
    iconSize: [35, 40],
    iconAnchor: [40, 45],
    popupAnchor: [-15, -30],
    className: 'myDivIcon'
  });
  // Icono de parada que pertenece al recorrido.
  iconDivIn = L.divIcon({
    html: '<i class="bi bi-geo-fill" style="font-size: 30px; color:blue"></i>',
    iconSize: [35, 40],
    iconAnchor: [40, 45],
    popupAnchor: [-15, -30],
    className: 'myDivIcon'
  });

  @ViewChild('scroller') scroller: CdkVirtualScrollViewport;
  
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
   * Elimina la ultima parada del recorrido de la lista y del mapa y el recorrido entre la ultima y anteultima parada.
   */
  quitarUltimaParada() {
    const len = this.paradasRecorrido.length;
    if (len <= 2) {
      const paradaRemove = this.paradasRecorrido.pop();
      this.addParadaToDisponibles( paradaRemove );
      if (this.control) {
        this.control.setWaypoints([]);
        this.control.remove();
        this.control = null;
      }
    }
    else { // if (len > 2) 
      const wps = this.control.getWaypoints();
      const anteultimaParada = this.paradasRecorrido[len - 2];
      let lastwp = wps.pop();
      do {
        if (lastwp.latLng.lat != anteultimaParada.coordenada.lat && lastwp.latLng.lng != anteultimaParada.coordenada.lng) {
          lastwp = wps.pop();
        }
        else {
          wps.push(lastwp);
          lastwp = null;
        }
      }
      while (lastwp != null);
      const paradaRemove = this.paradasRecorrido.pop();
      this.addParadaToDisponibles( paradaRemove );
      this.control.setWaypoints(wps);
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
          show: false, autoRoute: true, collapsible: true, 
          plan: L.Routing.plan([anteultima, ultima], {
            addWaypoints: true, draggableWaypoints: true, 
            createMarker: (i, wp, n) => {
              let marker: L.Marker;
              if (i == 0 || i == n - 1)
                marker = L.marker(wp.latLng, { icon: this.iconDivIn, draggable: false });
              return marker;
            }
          }),
          lineOptions: { styles: [ { color: 'red', weight: 5 }], extendToWaypoints: false, missingRouteTolerance: 5 }
        }).addTo(this.map);
        // Oculta el itinerario
        const routingControlContainer = this.control.getContainer();
        const controlContainerParent = routingControlContainer.parentNode;
        controlContainerParent.removeChild(routingControlContainer);
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
      }
    }
  }

  /**
   * Agrega una parada a la lista de disponibles y ordena la lista por codigo.
   * @param parada 
   */
  addParadaToDisponibles( parada: any ) {
    this.paradasDisponibles.push( parada );
    this.paradasDisponibles.sort( (p1: Parada, p2:Parada ) => {
      if (p1.codigo < p2.codigo) return -1;
      else if (p1.codigo > p2.codigo) return 1;
      return 0;
    });
  }

  getRecorrido() {
    console.log("Recuperar recorrido del linea " + this.linea.denominacion );
  }

  getParadasRecorrido() {
    console.log("Recuperar listado de paradas de un recorrido de linea " + this.linea.denominacion );
  }

  guardarRecorrido() {
    if (!this.control || this.control.getWaypoints().length ==0) {
      this._msg.showMessage( 'No se definio recorrido','ERROR');
      return;
    }
    this.waypoints = this.control.getWaypoints().map( (wp:any) => {
      return { lat: wp.latLng.lat, lng: wp.latLng.lng }
    });
    this.recorrido = { id: null, activo: true, fechaInicio: new Date(), fechaFin: null, 
      Linea: this.linea, 
      trayectos: null,
      waypoints: this.waypoints
    }
    console.log("Guardar recorrido: ");
    console.log("recorrido: ", this.recorrido );
    console.log("waypoints: ", this.waypoints );
    console.log("paradas: ", this.paradasRecorrido );
  }

  actualizarRecorrido() {
    console.log("Actualizar recorrido: ");
  }
}
