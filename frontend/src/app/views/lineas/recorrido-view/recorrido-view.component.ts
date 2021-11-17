import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Linea } from 'src/app/data/linea';
import { Recorrido } from 'src/app/data/recorrido';
import { LineaService } from 'src/app/services/linea.service';

@Component({
  selector: 'app-recorrido-view',
  templateUrl: './recorrido-view.component.html',
  styleUrls: ['./recorrido-view.component.css']
})
export class RecorridoViewComponent implements OnInit {

  spin: boolean;
  id: any;
  linea: Linea;
  recorrido: Recorrido;

  constructor( private serviceLinea: LineaService,
              private _matsnack: MatSnackBar,
              private router: Router,
              private route: ActivatedRoute ) { }

  ngOnInit(): void {
    this.spin = true;
    
  }

}
