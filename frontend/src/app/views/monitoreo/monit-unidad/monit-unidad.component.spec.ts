import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonitUnidadComponent } from './monit-unidad.component';

describe('MonitUnidadComponent', () => {
  let component: MonitUnidadComponent;
  let fixture: ComponentFixture<MonitUnidadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MonitUnidadComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MonitUnidadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
