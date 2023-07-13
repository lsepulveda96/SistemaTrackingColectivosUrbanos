import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { Colectivo } from 'src/app/data/colectivo';
import { MonitorService } from 'src/app/services/monitor.service';

@Component({
  templateUrl: './monit-unidad.component.html',
  styleUrls: ['./monit-unidad.component.css']
})
export class MonitUnidadComponent implements OnInit {

  waiting: boolean;
  colectivo: Colectivo;
  id: number;

  constructor(
    private serviceMonitor: MonitorService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
      const idUnidad = this.route.snapshot.paramMap.get('id');
      this.getColectivoEnTransito( parseInt(idUnidad ));
  }

  getColectivoEnTransito( idUnidad: number ) {
    this.waiting = true;
    this.serviceMonitor.getUnidadRecorridoTransito( idUnidad )
      .subscribe( result => {
        this.waiting = false;
      });
  }

}
