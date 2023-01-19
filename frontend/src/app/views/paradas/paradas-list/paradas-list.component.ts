import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Parada } from 'src/app/data/parada';
import { ParadaService } from 'src/app/services/parada.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-paradas-list',
  templateUrl: './paradas-list.component.html',
  styleUrls: ['./paradas-list.component.css']
})
export class ParadasListComponent implements OnInit {

  spin: boolean = false;
  paradas: Parada[] = [];
  paradasDS: MatTableDataSource<Parada> = new MatTableDataSource<Parada>([]);
  
  isadmin: boolean = false;

  constructor( 
    private serviceParada: ParadaService,
    private tokenService: TokenStorageService ) { 
      this.isadmin = tokenService.isUserAdmin();
  }

  ngOnInit(): void {
    this.getParadas();
  }

  getParadas() {
    this.spin = true;
    this.serviceParada.getParadas()
      .subscribe( result => {
        this.paradas = result.data;
        this.paradasDS.data = this.paradas;
        this.spin = false;
      });
  }
}
