import { AfterViewInit, Component, OnInit } from '@angular/core';

import * as L from 'leaflet';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit, AfterViewInit {

  private map: any;

  constructor() { }

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    this.initMap();
  }

  private initMap() {
    this.map = L.map( 'map', {
      center: [-42.785935, -65.012144],
      zoom: 14
    });

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      minZoom: 3,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });

    tiles.addTo(this.map);
  }
  

}
