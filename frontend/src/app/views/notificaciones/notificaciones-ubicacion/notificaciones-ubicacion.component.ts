import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import * as L from 'leaflet';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-notificaciones-ubicacion',
  templateUrl: './notificaciones-ubicacion.component.html',
  styleUrls: ['./notificaciones-ubicacion.component.css']
})
export class NotificacionUbicacionComponent implements OnInit {

  marker: any;
  waiting: boolean;
  notif: any;
  notifData: any;
  width: string;
  map: any;
  iconImg = '../../../assets/images/stcuadmin_icon.png';
  iconParada: any = L.icon({
    iconUrl: 'assets/images/buspin.png',
    iconSize: [45, 50],
    iconAnchor: [45, 50],
    popupAnchor: [-25, -45]
  });

  constructor(
    private route: ActivatedRoute,
    public dialogRef: MatDialogRef<NotificacionUbicacionComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) { }

  ngOnInit(): void {
    this.notif = this.data.notif;
    //console.log("latitud traida: " +  this.coordenada.lat);
    //console.log("longitud traida: " +  this.coordenada.lng);
    this.initMap();
    console.log(JSON.stringify(this.notif));


    this.width = this.dialogRef.componentInstance.width;

    /*this.qrdata = JSON.stringify({
      linea: this.linea.id,
      parada: this.parada.codigo,
      par: this.parada.direccion,
      recorrido: this.recorrido.id,
      rec2: this.recorrido.denominacion,
    });*/

    this.notifData = JSON.stringify({
      notif: this.notif
    });
    this.notifData = this.notifData.slice(1,-1); // para eliminar corchetes al inicio y al final 


    if (this.marker)
    this.map.removeLayer(this.marker);
  this.marker = L.marker([this.notif.coordenada.lat, this.notif.coordenada.lng], { icon: this.iconParada, draggable: false })
    .addTo(this.map)
    .bindPopup('Unidad: ' + this.notif.colectivoDenom)
    .openPopup();
  this.map.panTo(new L.LatLng(this.notif.coordenada.lat, this.notif.coordenada.lng));


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

  cerrar() {
    this.dialogRef.close();
  }



}
