import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';

import { Parada } from 'src/app/data/parada';
import { ParadaService } from 'src/app/services/parada.service';

import { ESRI_PARAMS } from 'src/app/services/esriConfig';

import * as L from 'leaflet';

import 'esri-leaflet-geocoder/dist/esri-leaflet-geocoder.css';
import 'esri-leaflet-geocoder';
import * as ELG from 'esri-leaflet-geocoder';

@Component({
  selector: 'app-parada-edit',
  templateUrl: './parada-edit.component.html',
  styleUrls: ['./parada-edit.component.css']
})
export class ParadaEditComponent implements OnInit {

  spin: boolean;
  codigo: string;
  parada: Parada;

  direccionIC = new UntypedFormControl('', Validators.required);
  descripcionIC = new UntypedFormControl('');

  map: any;
  marker: any;
  iconParada: any = L.icon({
    iconUrl: 'assets/images/stopbus.png',
    iconSize: [45, 50],
    iconAnchor: [45, 50],
    popupAnchor: [-8, -37]
  });;
  geocodeService: any;

  constructor(private serviceParada: ParadaService,
    private _snackbar: MatSnackBar,
    private router: Router,
    private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.codigo = this.route.snapshot.paramMap.get('codigo');

    this.initMap();
    if (!this.codigo)
      this.nuevaParada();
    else {
      this.editarParada(parseInt(this.codigo));
    }
  }

  /**
   * Inicializa y prepara pantalla para registrar nueva parada.
   */
  nuevaParada() {
    this.spin = true;
    this.direccionIC.setValue('');
    this.descripcionIC.setValue('');
  }

  /**
   * Recupera los datos de la parada, inicializa los campos y habilita pantalla para edicion.
   * @param codigo 
   */
  editarParada(codigo: number) {
    this.spin = true;
    this.serviceParada.getParada(codigo).subscribe(result => {
      this.spin = false;
      if (result.error) {
        this._snackbar.open(result.mensaje, '', {
          duration: 4500,
          verticalPosition: 'top', // 'top' | 'bottom'
          horizontalPosition: 'end', //'start' | 'center' | 'end' | 'left' | 'right'
          panelClass: ['red-snackbar'],
        });
        this.router.navigate(['../..'], { relativeTo: this.route });
      }
      this.parada = result.data;
      this.descripcionIC.setValue(this.parada.descripcion);
      this.direccionIC.setValue(this.parada.direccion);
      this.marker = L.marker([this.parada.coordenada.lat, this.parada.coordenada.lng], { icon: this.iconParada })
        .addTo(this.map)
        .bindPopup(this.parada.direccion)
        .openPopup();
    });
  }

  /**
   * Inicializa div con mapa de la ciudad y habilita para indicar parada mediante click.
   */
  private initMap() {
    this.map = L.map('map', {
      center: [-42.775935, -65.038144],
      zoom: 14
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      minZoom: 12,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(this.map);

    this.geocodeService = ELG.geocodeService({
      apiKey: ESRI_PARAMS.apiKey,
      token: ESRI_PARAMS.token
    });

    this.map.on('click', (e: any) => {
      this.streetMarker(e);
    });
  }

  /**
   * Ajusta las coordenadas a la calle.
   * @param point 
   */
  streetMarker(point: any) {
    this.geocodeService.reverse().latlng(point.latlng).run((error: any, result: any) => { // Ajusta direccion a calle.
      if (error) {  // Si hay error muestra el mensaje.
        this._snackbar.open('No se pudo obtener direccion, ingrese manualmente', '', {
          duration: 4000, verticalPosition: 'bottom', horizontalPosition: 'center',
          panelClass: ['yellow-snackbar']
        });
        this.addMarker( point );
      }
      else // Si hay resultado se carga la parada en la direccion ajustada.
        this.addMarker( result );
    });
  }

  /**
   * Agrega marcador de parada en el mapa en la coordenada indicada.
   * @param coord 
   */
  addMarker(coord: any) {
    if (this.marker && this.map.hasLayer(this.marker)) // Si ya esta la parada en el mapa se elimina.
        this.map.removeLayer(this.marker);
        
    this.marker = L.marker(coord.latlng , { icon: this.iconParada }).addTo(this.map);
    fetch(`https://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer/reverseGeocode?f=pjson&langCode=ES&featureTypes=StreetAddress&locationType=street&location=${coord.latlng.lng},${coord.latlng.lat}`)
      .then(res => res.json())
      .then(myJson => {
        this.marker.bindPopup(myJson.address.Address).openPopup();
        this.direccionIC.setValue( myJson.address.Address );
      })
      .catch(err => console.log("ERROR: ", err));
  }

  /**
   * Registra la parada
   * @returns 
   */
  guardarParada() {
    if (!this.marker)
      return;
    this.parada = {
      codigo: null,
      direccion: this.direccionIC.value,
      descripcion: this.descripcionIC.value,
      estado: 'HABILITADA',
      coordenada: {
        lat: this.marker.getLatLng().lat,
        lng: this.marker.getLatLng().lng
      }
    }
    this.spin = true;
    this.serviceParada.saveParada(this.parada).subscribe(result => {
      this.spin = false;
      this._snackbar.open(result.mensaje, '', {
        duration: 4500,
        verticalPosition: 'top', // 'top' | 'bottom'
        horizontalPosition: 'end', //'start' | 'center' | 'end' | 'left' | 'right'
        panelClass: result.error ? ['red-snackbar'] : ['blue-snackbar'],
      });
      if (!result.error)
        this.router.navigate(['../'], { relativeTo: this.route });
    });

  }

  /**
   * Actualiza los datos de una parada
   */
  actualizarParada() {
    this.parada.direccion = this.direccionIC.value;
    this.parada.descripcion = this.descripcionIC.value;
    this.parada.coordenada = {
      lat: this.marker.getLatLng().lat,
      lng: this.marker.getLatLng().lng
    }
    this.spin = true;
    this.serviceParada.updateParada(this.parada).subscribe(result => {
      this.spin = false;
      this._snackbar.open(result.mensaje, '', {
        duration: 4500,
        verticalPosition: 'top', // 'top' | 'bottom'
        horizontalPosition: 'end', //'start' | 'center' | 'end' | 'left' | 'right'
        panelClass: result.error ? ['red-snackbar'] : ['blue-snackbar'],
      });
      if (!result.error)
        this.router.navigate(['../..'], { relativeTo: this.route });
    });
  }
}

