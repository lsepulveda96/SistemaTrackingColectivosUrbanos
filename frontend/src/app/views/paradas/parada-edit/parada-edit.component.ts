import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';

import { Parada } from 'src/app/data/parada';
import { ParadaService } from 'src/app/services/parada.service';

import { ESRI_PARAMS } from 'src/app/services/esriConfig';


import * as L from 'leaflet';
import * as esri from 'esri-leaflet';
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


  nuevaParada() {
    this.spin = true;
    this.direccionIC.setValue('');
    this.descripcionIC.setValue('');
  }

  editarParada(codigo: number) {
    this.spin = true;
    this.serviceParada.getParada(codigo)
      .subscribe(result => {
        if (result.error) {
          this._snackbar.open(result.mensaje, '', {
            duration: 4500,
            verticalPosition: 'top', // 'top' | 'bottom'
            horizontalPosition: 'end', //'start' | 'center' | 'end' | 'left' | 'right'
            panelClass: ['red-snackbar'],
          });
          this.router.navigate(['../..'], { relativeTo: this.route });
          this.spin = false;
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

  private initMap() {

    this.map = L.map('map', {
      center: [-42.775935, -65.038144],
      zoom: 14
    });
    // this.map.setView( [-42.775935, -65.038144],14 );

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      minZoom: 12,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(this.map);

    // const basemapEnum = "ArcGIS:Topographic"
    // const layer = esri.BasemapLayer(basemapEnum, { apiKey: ESRI_PARAMS.apiKey });
    // layer.addTo(this.map);

    // const searchControl = new ELG.Geosearch( {
    //   providers: [ELG.arcgisOnlineProvider( { apiKey: ESRI_PARAMS.apiKey })]
    // });

    // const results = new L.LayerGroup().addTo( this.map );

    // searchControl.on("results", (data) => {
    //   console.log( "On evnet: ", data );
    //     results.clearLayers();
    //     for(let i = data.results.length -1 ; i >=0; i--) {
    //       results.addLayer( L.marker( data.results[i].latlng, { icon: this.iconParada, draggable: false } ));
    //     }
    // }).addTo( this.map );

    this.geocodeService = ELG.geocodeService({
      apiKey: ESRI_PARAMS.apiKey,
      token: ESRI_PARAMS.token
    });

    this.map.on('click', (e) => {
      if (this.marker && this.map.hasLayer(this.marker))
        this.map.removeLayer(this.marker);
      this.marker = L.marker(e.latlng, { icon: this.iconParada }).addTo(this.map);
      this.spin = true;
      this.geocodeService.reverse().latlng(e.latlng).run((error, result) => {
        console.log("reverse error: ", error);
        this.spin = false;
        if (error) {
          this._snackbar.open('No se pudo obtener direccion, ingrese manualmente', '', {
            duration: 4000,
            verticalPosition: 'bottom',
            horizontalPosition: 'center',
            panelClass: ['yellow-snackbar']
          });
          this.direccionIC.setValue('');
        }
        else {
          if (this.marker && this.map.hasLayer(this.marker))
            this.map.removeLayer(this.marker);
          this.marker = L.marker(result.latlng, { icon: this.iconParada })
            .addTo(this.map)
            .bindPopup(result.address.Address)
            .openPopup();
          this.direccionIC.setValue(result.address.Address);
        }
      });
    });
  }



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
    this.serviceParada.saveParada(this.parada)
      .subscribe(result => {
        this._snackbar.open(result.mensaje, '', {
          duration: 4500,
          verticalPosition: 'top', // 'top' | 'bottom'
          horizontalPosition: 'end', //'start' | 'center' | 'end' | 'left' | 'right'
          panelClass: result.error ? ['red-snackbar'] : ['blue-snackbar'],
        });
        if (!result.error)
          this.router.navigate(['../'], { relativeTo: this.route });
        this.spin = false;
      });

  }

  actualizarParada() {
    this.parada.direccion = this.direccionIC.value;
    this.parada.descripcion = this.descripcionIC.value;
    this.parada.coordenada = {
      lat: this.marker.getLatLng().lat,
      lng: this.marker.getLatLng().lng
    }


    console.log("Actualizar Parada ", this.parada);
    this.spin = true;
    this.serviceParada.updateParada(this.parada)
      .subscribe(result => {
        this._snackbar.open(result.mensaje, '', {
          duration: 4500,
          verticalPosition: 'top', // 'top' | 'bottom'
          horizontalPosition: 'end', //'start' | 'center' | 'end' | 'left' | 'right'
          panelClass: result.error ? ['red-snackbar'] : ['blue-snackbar'],
        });
        if (!result.error)
          this.router.navigate(['../..'], { relativeTo: this.route });
        this.spin = false;
      });
  }
}

