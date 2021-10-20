import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';

import { Parada } from 'src/app/data/parada';
import { ParadaService } from 'src/app/services/parada.service';

import { APPCONFIG } from 'src/app/services/appconfig';


import * as L from 'leaflet';
import * as esri from 'esri-leaflet';
import * as esrigeo from 'esri-leaflet-geocoder';
import { TokenizeResult } from '@angular/compiler/src/ml_parser/lexer';


@Component({
  selector: 'app-parada-edit',
  templateUrl: './parada-edit.component.html',
  styleUrls: ['./parada-edit.component.css']
})
export class ParadaEditComponent implements OnInit {

  spin: boolean;
  codigo: string;
  parada: Parada;

  direccionIC = new FormControl('',Validators.required );
  descripcionIC = new FormControl('');

  map: any;
  marker: any;
  iconOptions: any;
  geocodeService: any;

  constructor( private serviceParada: ParadaService,
              private _snackbar: MatSnackBar,
              private router: Router,
              private route: ActivatedRoute ) { }

  ngOnInit(): void {
    this.codigo = this.route.snapshot.paramMap.get('codigo');

    this.iconOptions = L.icon( {
      iconUrl: 'assets/images/stopbus.png',
      iconSize: [45,50],
      iconAnchor: [45,50],
      popupAnchor: [-3,-20]
      });

    this.initMap();
    if (!this.codigo)
      this.nuevaParada();
    else {
      this.editarParada( parseInt( this.codigo ));
    }   
  }


  nuevaParada() {
    this.spin = true;
    this.direccionIC.setValue('');
    this.descripcionIC.setValue('');
  }

  editarParada( codigo: number ) {
    this.spin = true;
    this.serviceParada.getParada( codigo )
      .subscribe( result => {
        if (result.error) {
          this._snackbar.open( result.mensaje, '', {
            duration: 4500,
            verticalPosition: 'top', // 'top' | 'bottom'
            horizontalPosition: 'end', //'start' | 'center' | 'end' | 'left' | 'right'
            panelClass: ['red-snackbar'],
          });
          this.router.navigate( ['../..'], { relativeTo: this.route });
          this.spin = false;  
        }
        this.parada = result.data;
        this.descripcionIC.setValue( this.parada.descripcion );
        this.direccionIC.setValue( this.parada.direccion);
        
        this.marker = L.marker( [this.parada.lat, this.parada.lng], { icon: this.iconOptions, draggable:true });
        this.marker.on('dragend', (e2) => {
            this.spin = true;
            console.log("DRAGGED : ", e2 );
            this.geocodeService.reverse().latlng( this.marker.getLatLng() )
              .run( (error2, result2) => {
                this.spin = false;
                if (error2)
                  return;
                //this.marker.setLatLng( e2.target.latlng );
                const popup2 = L.popup().setContent( result2.address.Match_addr ).setLatLng( this.marker.getLatLng() );
                this.map.openPopup( popup2 );
                this.direccionIC.setValue( result2.address.Match_addr );
              });
          })
          const popup = L.popup().setContent( this.parada.direccion ).setLatLng( new L.LatLng( this.parada.lat, this.parada.lng ) );
          this.map.openPopup(popup);
          this.map.addLayer( this.marker );
          
      }); 
  }

  private initMap() {

    this.map = L.map( 'map', {
      center: [-42.775935, -65.038144],
      zoom: 14
    });

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      minZoom: 12,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });
 
    this.geocodeService = esrigeo.geocodeService( {
      token: 'ACpVQC8C0-B7Vft2qAYgOxa9wpEf6YBzMOlxft6B_NKTdyZ8NJHmQlg0YczePobaQUXdn8qRSZC97OPGhxXcqU0PN3endAXedOYwj_EeyjwdqhFW4YMaOCwz0WVOidNv_P_OjDMzdkgs_avpo1KFVw..'
    });

    tiles.addTo( this.map );
    this.map.on('click', (e) => {

      this.spin = true;
      if (this.marker)
        this.map.removeLayer( this.marker );

      this.geocodeService.reverse().latlng( e.latlng )
        .run( (error,result ) => {
          this.spin = false;
          console.log("error: ", error );
          console.log("result: ", result );
          if (error)
            return;
          const msg: string = result.address.Match_addr;

          this.marker = L.marker( e.latlng, { icon: this.iconOptions, draggable:true });
          this.marker.on('dragend', (e2) => {
            this.spin = true;
            this.geocodeService.reverse().latlng( this.marker.getLatLng() )
              .run( (error2, result2) => {
                this.spin = false;
                if (error2)
                  return;
                //this.marker.setLatLng( e2.latlng );
                const popup2 = L.popup().setContent( result2.address.Match_addr ).setLatLng( this.marker.getLatLng() );
                this.map.openPopup( popup2 );
                this.direccionIC.setValue( result2.address.Match_addr );
              });
          })
          const popup = L.popup().setContent( result.address.Match_addr ).setLatLng(e.latlng);
          this.map.openPopup(popup);
          this.map.addLayer( this.marker );
          this.direccionIC.setValue( msg );
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
      lat: this.marker.getLatLng().lat, 
      lng: this.marker.getLatLng().lng 
    }

    this.spin = true;
    this.serviceParada.saveParada( this.parada )
      .subscribe( result => {
        this._snackbar.open( result.mensaje, '', {
          duration: 4500,
          verticalPosition: 'top', // 'top' | 'bottom'
          horizontalPosition: 'end', //'start' | 'center' | 'end' | 'left' | 'right'
          panelClass: result.error ? ['red-snackbar']:['blue-snackbar'],
        });
        if (!result.error) 
          this.router.navigate( ['../'], { relativeTo: this.route });
        this.spin = false;
      });

  }

  actualizarParada() {
    this.parada.direccion = this.direccionIC.value;
    this.parada.descripcion = this.descripcionIC.value;
    this.parada.lat = this.marker.getLatLng().lat;
    this.parada.lng = this.marker.getLatLng().lng;

    console.log("Actualizar Parada ", this.parada);
    this.spin = true;
    this.serviceParada.updateParada( this.parada )
      .subscribe( result => {
        this._snackbar.open( result.mensaje, '', {
          duration: 4500,
          verticalPosition: 'top', // 'top' | 'bottom'
          horizontalPosition: 'end', //'start' | 'center' | 'end' | 'left' | 'right'
          panelClass: result.error ? ['red-snackbar']:['blue-snackbar'],
        });
        if (!result.error) 
          this.router.navigate( ['../..'], { relativeTo: this.route });
        this.spin = false;
      });
  }
}

