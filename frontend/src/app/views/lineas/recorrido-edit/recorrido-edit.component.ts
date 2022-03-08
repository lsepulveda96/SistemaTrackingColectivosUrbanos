import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Linea } from 'src/app/data/linea';
import { Recorrido } from 'src/app/data/recorrido';
import { LineaService } from 'src/app/services/linea.service';

@Component({
  selector: 'app-recorrido-edit',
  templateUrl: './recorrido-edit.component.html',
  styleUrls: ['./recorrido-edit.component.css']
})
export class RecorridoEditComponent implements OnInit {

  modoNew: boolean;
  waiting: boolean;
  linea: Linea;
  recorrido: Recorrido;

  constructor( private servicioLinea: LineaService, 
              private snackbar: MatSnackBar,
              private route: ActivatedRoute,
              private router: Router ) { }

  ngOnInit(): void {
    const mod = this.route.snapshot.paramMap.get("modo");
    if (!mod) {
      this.snackbar.open( 'Error no se indico modo nuevo o edicion','', {
        duration: 3500,
      });
      this.router.navigate(['../..']);
    } 
    this.modoNew = (mod == 'new');
    const id = this.route.snapshot.paramMap.get('id');
    this.waiting = true;
    this.servicioLinea.getLinea( parseInt(id ))
      .subscribe( result => {
        this.waiting = false;
        this.linea = result.data;
      });
    if (this.modoNew)
      this.nuevoRecorrido();
    else  
      this.editarRecorrido( parseInt( id ));
  }

  nuevoRecorrido() {

  }

  editarRecorrido( id: number) {
    this.waiting = true;
    this.servicioLinea.getRecorrido( id )
      .subscribe( result => {
        this.waiting = false;
        if (!result.error)
          this.recorrido = result.data;
      });
  }


  guardarRecorrido() {

  }

  actualizarRecorrido() {
    
  }
}
