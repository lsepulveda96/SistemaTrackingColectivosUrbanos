import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { Colectivo } from 'src/app/data/colectivo';
import { ColectivoService } from 'src/app/services/colectivo.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-colectivos-list',
  templateUrl: './colectivos-list.component.html',
  styleUrls: ['./colectivos-list.component.css']
})
export class ColectivosListComponent implements OnInit {

  spin: boolean = false;
  colectivos: Colectivo[] = [];
  colectivosDS: MatTableDataSource<Colectivo> = new MatTableDataSource<Colectivo>([]);

  isadmin: boolean = false;

  constructor( 
    private serviceColectivo: ColectivoService,
    private tokenService: TokenStorageService ) { 
      this.isadmin = tokenService.isUserAdmin();
  }

  ngOnInit(): void {
    this.getColectivos();
  }

  getColectivos() {
    this.spin = true;
    this.serviceColectivo.getColectivos() 
      .subscribe( result => {
          this.colectivos = result.data;
          this.colectivosDS.data = this.colectivos;
          this.spin = false;
      });
  }

}
