import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Parada } from 'src/app/data/parada';
import { ParadaService } from 'src/app/services/parada.service';

import * as L from 'leaflet';

@Component({
  selector: 'app-parada-view',
  templateUrl: './parada-view.component.html',
  styleUrls: ['./parada-view.component.css']
})
export class ParadaViewComponent implements OnInit {

  waiting: boolean;
  parada: Parada;

  map: any;
  marker: any;
  iconParada: any = L.icon({
    iconUrl: 'assets/images/stopbus.png',
    iconSize: [45, 50],
    iconAnchor: [45, 50],
    popupAnchor: [-8, -37]
  });;

  constructor( private servicioParada: ParadaService,
              private _snackbar: MatSnackBar,
              private route: ActivatedRoute,
              private router: Router ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('codigo');
    if (id)  {
      this.initMap();
      this.getParada( parseInt( id ));      
    }
  }

  getParada( id: number ) {
    this.waiting = true;
    this.servicioParada.getParada( id ) 
      .subscribe( result => {
        this.waiting = false;
        if (result.error) {
          this._snackbar.open( result.mensaje, '', {
            duration: 4500,
            verticalPosition: 'top', // 'top' | 'bottom'
            horizontalPosition: 'end', //'start' | 'center' | 'end' | 'left' | 'right'
            panelClass: ['red-snackbar'],
          });
          this.router.navigate( ['../..'], { relativeTo: this.route }); 
        }
        this.parada = result.data;
        
        this.marker = L.marker( [this.parada.coordenada.lat, this.parada.coordenada.lng], { icon: this.iconParada, draggable:false })
          .addTo( this.map )
          .bindPopup( this.parada.direccion )
          .openPopup();
      });
  }

  private initMap() {
    this.map = L.map( 'map', {
      center: [-42.775935, -65.038144],
      zoom: 14
    });
    
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      minZoom: 12,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo( this.map );
  }

}
