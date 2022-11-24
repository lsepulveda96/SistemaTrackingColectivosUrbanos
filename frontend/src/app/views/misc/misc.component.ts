import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-misc',
  template: `
  <router-outlet></router-outlet>
  `
})
export class MiscComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
