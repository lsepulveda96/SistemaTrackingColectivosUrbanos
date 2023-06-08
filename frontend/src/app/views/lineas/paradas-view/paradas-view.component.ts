import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';

import * as L from 'leaflet';
import { Linea } from 'src/app/data/linea';
import { Parada } from 'src/app/data/parada';
import { LineaService } from 'src/app/services/linea.service';

@Component({
  selector: 'app-paradas-view',
  templateUrl: './paradas-view.component.html',
  styleUrls: ['./paradas-view.component.css']
})
export class ParadasViewComponent implements OnInit {

  modeNew: boolean;
  waiting: boolean;
  linea: Linea;
  paradas: Parada[];
  map: L.Map;

  constructor( private servicioLinea: LineaService, 
    private snackbar: MatSnackBar,
    private route: ActivatedRoute,
    private router: Router ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.waiting = true;
    this.servicioLinea.getLinea( parseInt(id ))
      .subscribe( result => {
        this.waiting = false;
        this.linea = result.data;
      });
    this.servicioLinea.getRecorridosActivos( parseInt(id))
      .subscribe( result => {
        
      });
      
  }

}
