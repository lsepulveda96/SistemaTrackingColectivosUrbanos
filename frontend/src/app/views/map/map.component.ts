import { AfterViewInit, Component, OnInit } from '@angular/core';

import { ESRI_PARAMS } from 'src/app/services/esriConfig';

import * as L from 'leaflet';
import * as esri from 'esri-leaflet';
import * as ELG from 'esri-leaflet-geocoder';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit, AfterViewInit {

  private map: any;
  private marker: any;

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

    const argisOnline = ELG.arcgisOnlineProvider( {
      apiKey: ESRI_PARAMS.apiKey
    });
    const searchControl = new ELG.Geosearch( {
      providers: [argisOnline],
    });

    const geocodeService = ELG.geocodeService({
      token: ESRI_PARAMS.token
    });
    
    const results = new L.LayerGroup().addTo( this.map );

    searchControl.on("results", (data) => {
      console.log( "On evnet: ", data );
        results.clearLayers();
        for(let i = data.results.length -1 ; i >=0; i--) {
          results.addLayer( L.marker( data.results[i].latlng ));
        }
    }).addTo( this.map );

    this.map.on("click",  (e) => {
      geocodeService.reverse().latlng( e.latlng ).run( (error, result ) => {
        if (error) {
          console.log("Reverse error: ", error );
          return;
        }
        if (this.marker && this.map.hasLayer( this.marker ))
          this.map.removeLayer( this.marker );
        this.marker = L.marker( result.latlng )
          .addTo( this.map )
          .bindPopup( result.address.Match_addr )
          .openPopup();
      });
    });
  }
  

}
