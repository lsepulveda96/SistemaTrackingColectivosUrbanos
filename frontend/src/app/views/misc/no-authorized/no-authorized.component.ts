import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-no-authorized',
  templateUrl: './no-authorized.component.html',
  styleUrls: ['./no-authorized.component.css']
})
export class NoAuthorizedComponent implements OnInit {

  constructor( private location: Location) { }

  ngOnInit(): void {
  }

  volver() {
    console.log("Volver");
    console.log("History: ", this.location.path() );
    //this.location.historyGo
  }
}
