import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Linea } from 'src/app/data/linea';
import { LineaService } from 'src/app/services/linea.service';
import { ParadaService } from 'src/app/services/parada.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-lineas-list',
  templateUrl: './lineas-list.component.html',
  styleUrls: ['./lineas-list.component.css']
})
export class LineasListComponent implements OnInit {

  waiting: boolean = true;
  lineas: Linea[] = [];
  lineasDS: MatTableDataSource<Linea> = new MatTableDataSource<Linea>([]);

  isadmin: boolean = false;

  constructor( 
    private serviceLinea: LineaService,
    private tokenService: TokenStorageService ) {
      this.isadmin= tokenService.isUserAdmin();
  }

  ngOnInit(): void {
    this.getLineas();
  }

  getLineas() {
    this.waiting = true;
    this.serviceLinea.getLineas()
      .subscribe( result => {
        this.lineas = result.data;
        this.lineasDS.data = this.lineas;
        this.waiting = false;
      });
  }
}
