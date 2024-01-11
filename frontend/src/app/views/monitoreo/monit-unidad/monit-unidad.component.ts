import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { MonitorService } from 'src/app/services/monitor.service';
import { ColectivoRecorrido } from 'src/app/data/colectivoRecorrido';
import * as L from 'leaflet';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Coordenada } from 'src/app/data/coordenada';

@Component({
  selector: 'app-monit-unidad',
  templateUrl: './monit-unidad.component.html',
  styleUrls: ['./monit-unidad.component.css']
})


export class MonitUnidadComponent implements OnInit {

  waiting: boolean;
  colectivoRecorrido: ColectivoRecorrido;
  coordenadaColeRec: Coordenada;
  id: number;

  map: any;
  marker: any;
  iconParada: any = L.icon({
    iconUrl: 'assets/images/stopbus.png',
    iconSize: [45, 50],
    iconAnchor: [45, 50],
    popupAnchor: [-8, -37]
  });;


  constructor(
    private serviceMonitor: MonitorService,
    private router: Router,
    private route: ActivatedRoute,
    private _snackbar: MatSnackBar
  ) { }

  ngOnInit(): void {

    this.route.url.subscribe((u) => {

      let listaParams = this.route.snapshot.params;
      console.log("undef?" + JSON.stringify(this.route.snapshot.params));

      for (let i = 0; i < listaParams.length; i++) {
        console.log("data params: " + JSON.stringify(listaParams[i]));
      }

    });


    const idUnidad = this.route.snapshot.paramMap.get('id');
    this.getColectivoEnTransito(parseInt(idUnidad));
    console.log("unidad: " + this.route.snapshot.paramMap.get('id'));
    this.getCoordenadasColectivoEnTransito(parseInt(idUnidad));
    this.initMap();
  }

  getColectivoEnTransito(idUnidad: number) {
    this.waiting = true;
    this.serviceMonitor.getUnidadRecorridoTransito(idUnidad)
      .subscribe(result => {
        this.waiting = false;
        this.colectivoRecorrido = result.data;
        console.log("lo que trae getColectivoEnTransito: "+ JSON.stringify(this.colectivoRecorrido));
      });
  }

  /*
    getCoordenadasColectivoEnTransito( idUnidad: number ) {
      this.waiting = true;
      this.serviceMonitor.getCoordenadasColectivoRecorrido(idUnidad)
        .subscribe( result => {
          this.waiting = false;
          this.coordenadaColeRec = result.data;
          console.log("coordenadas: " + this.coordenadaColeRec);
        });
    }
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
  }




  getCoordenadasColectivoEnTransito(idUnidad: number) {
    this.waiting = true;
    this.serviceMonitor.getCoordenadasColectivoRecorrido(idUnidad)
      .subscribe(result => {
        this.waiting = false;
        if (result.error) {
          this._snackbar.open(result.mensaje, '', {
            duration: 4500,
            verticalPosition: 'top', // 'top' | 'bottom'
            horizontalPosition: 'end', //'start' | 'center' | 'end' | 'left' | 'right'
            panelClass: ['red-snackbar'],
          });
          this.router.navigate(['../..'], { relativeTo: this.route });
        }
        this.coordenadaColeRec = result.data;
        this.marker = L.marker([this.coordenadaColeRec.lat, this.coordenadaColeRec.lng], { icon: this.iconParada, draggable: false })
          .addTo(this.map)
          .openPopup();
      });
  }

}

