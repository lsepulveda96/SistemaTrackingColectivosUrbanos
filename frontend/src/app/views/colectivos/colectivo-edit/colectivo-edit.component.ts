import { Component, OnInit } from "@angular/core";
import { UntypedFormControl, Validators } from "@angular/forms";
import {
  MAT_MOMENT_DATE_ADAPTER_OPTIONS,
  MAT_MOMENT_DATE_FORMATS,
  MomentDateAdapter,
} from "@angular/material-moment-adapter";
import {
  DateAdapter,
  MAT_DATE_FORMATS,
  MAT_DATE_LOCALE,
} from "@angular/material/core";
import { MatDialog } from "@angular/material/dialog";
import { MatSnackBar } from "@angular/material/snack-bar";
import { ActivatedRoute, Router } from "@angular/router";
import { Colectivo } from "src/app/data/colectivo";
import { ColectivoService } from "src/app/services/colectivo.service";
import { ConfirmComponent } from "../../misc/confirm/confirm.component";
import { DomSanitizer } from "@angular/platform-browser";

@Component({
  selector: "app-colectivo-edit",
  templateUrl: "./colectivo-edit.component.html",
  styleUrls: ["./colectivo-edit.component.css"],
  providers: [
    { provide: MAT_DATE_LOCALE, useValue: "es-AR" },
    {
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS],
    },
    { provide: MAT_DATE_FORMATS, useValue: MAT_MOMENT_DATE_FORMATS },
  ],
})
export class ColectivoEditComponent implements OnInit {
  spin: boolean = true;
  id: any;
  colectivo: Colectivo | undefined;

  unidadIC = new UntypedFormControl("", Validators.required);
  patenteIC = new UntypedFormControl("", [
    Validators.required,
    Validators.pattern("^[A-Z]{2}[0-9]{3}[A-Z]{2}$|^[A-Z]{3}[0-9]{3}$"),
  ]);
  marcaIC = new UntypedFormControl("", Validators.required);
  modeloIC = new UntypedFormControl("");
  anioIC = new UntypedFormControl(
    null,
    Validators.pattern("^[12]{1}[09]{1}[0-9]{2}$")
  ); // 19xx o 20xx
  capacidadIC = new UntypedFormControl(
    null,
    Validators.pattern("^[1-9][0-9]?$")
  ); // numero entre 1 y 100
  compraIC = new UntypedFormControl(null);

  estados: string[] = ["HABILITADO", "NO HABILITADO"];
  estadoIC = new UntypedFormControl("");

  imagen: any;
  url: any;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private serviceColectivo: ColectivoService,
    private dialog: MatDialog,
    private _snackbar: MatSnackBar,
    private readonly sanitizer: DomSanitizer
  ) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get("id");
    if (this.id) this.editarColectivo(parseInt(this.id));
    else this.nuevoColectivo();
  }

  nuevoColectivo() {
    this.spin = true;
    this.unidadIC.setValue("");
    this.patenteIC.setValue("");
    this.marcaIC.setValue("");
    this.modeloIC.setValue("");
    this.anioIC.setValue(null);
    this.capacidadIC.setValue(null);
    this.compraIC.setValue(null);
    this.estadoIC.setValue(this.estados[0]);
    this.spin = false;
  }

  editarColectivo(id: number) {
    this.spin = true;
    this.serviceColectivo.getColectivo(id).subscribe((result) => {
      this.spin = false;
      if (result.error) {
        this._snackbar.open("No se pudo recuperar Colectivo " + id);
        return;
      } else {
        this.colectivo = result.data;
        this.unidadIC.setValue(this.colectivo.unidad);
        this.patenteIC.setValue(this.colectivo.patente);
        this.marcaIC.setValue(this.colectivo.marca);
        this.modeloIC.setValue(this.colectivo.modelo);
        this.anioIC.setValue(this.colectivo.anio);
        this.capacidadIC.setValue(this.colectivo.capacidad);
        this.compraIC.setValue(new Date(this.colectivo.fechaCompra));
        this.estadoIC.setValue(this.colectivo.estado);
        this.imagen = null;
        if (this.colectivo.imgpath) {
          this.serviceColectivo
            .downloadImagen(this.colectivo.imgpath)
            .subscribe((img) => {
              const blob = new Blob([img], { type: img.type });
              this.url = this.sanitizer.bypassSecurityTrustResourceUrl(
                window.URL.createObjectURL(blob)
              );
            });
        }
      }
    });
  }

  registrarNuevoColectivo() {
    if (this.imagen) {
      this.spin = true;
      this.serviceColectivo.uploadImagen(this.imagen).subscribe((result) => {
        this.spin = false;
        if (result.error) {
          this._snackbar.open(result.mensaje, "", {
            duration: 4500,
            verticalPosition: "bottom", // 'top' | 'bottom'
            horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
            panelClass: ["red-snackbar"],
          });
        }
        const filename = !result.error ? result.data : null;
        this.cargarValores(filename);
        this.guardar();
      });
    } else {
      this.cargarValores(null);
      this.guardar();
    }
  }

  private guardar() {
    this.spin = true;
    this.serviceColectivo.saveColectivo(this.colectivo).subscribe((result) => {
      this._snackbar.open(result.mensaje, "", {
        duration: 4500,
        verticalPosition: "top", // 'top' | 'bottom'
        horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
        panelClass: result.error ? ["red-snackbar"] : ["blue-snackbar"],
      });
      if (!result.error)
        this.router.navigate(["../"], { relativeTo: this.route });
      this.spin = false;
    });
  }

  actualizarColectivo() {
    if (this.imagen) {
      // si se cargo una imagen
      if (this.colectivo.imgpath && this.colectivo.imgpath.length > 0) {
        // si habia una imagen anterior se elimina
        this.serviceColectivo.deleteImagen(this.colectivo.imgpath).toPromise();
      }
      // Se carga la nueva imagen.
      this.spin = true;
      this.serviceColectivo.uploadImagen(this.imagen).subscribe((res) => {
        this.spin = false;
        if (res.error) {
          this._snackbar.open(res.mensaje, "", {
            duration: 4500,
            verticalPosition: "bottom", // 'top' | 'bottom'
            horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
            panelClass: ["red-snackbar"],
          });
        }
        const filename = !res.error ? res.data : null;
        this.cargarValores(filename);
        this.actualizar();
      });
    } else {
      this.cargarValores(null);
      this.actualizar();
    }
  }

  private actualizar() {
    this.spin = true;
    this.serviceColectivo
      .updateColectivo(this.colectivo)
      .subscribe((result) => {
        this._snackbar.open(result.mensaje, "", {
          duration: 4500,
          verticalPosition: "top", // 'top' | 'bottom'
          horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
          panelClass: result.error ? ["red-snackbar"] : ["blue-snackbar"],
        });
        if (!result.error)
          this.router.navigate(["../.."], { relativeTo: this.route });
        this.spin = false;
      });
  }

  cargarValores(filename: string) {
    this.colectivo = {
      id: this.id,
      unidad: this.unidadIC.value.toUpperCase(),
      marca: this.marcaIC.value.toUpperCase(),
      modelo: this.modeloIC.value.toUpperCase(),
      patente: this.patenteIC.value.toUpperCase(),
      anio: this.anioIC.value,
      capacidad: this.capacidadIC.value,
      fechaCompra: this.compraIC.value,
      estado: this.estadoIC.value,
      enCirculacion: false,
      fechaBaja: null,
      imgpath: filename,
    };
  }

  bajaColectivo() {
    const ref = this.dialog.open(ConfirmComponent, {
      data: {
        titulo: "Baja de colectivo",
        mensaje: "Confirma dar de baja unidad " + this.colectivo.unidad + "?",
      },
    });
    ref.afterClosed().subscribe((aceptar) => {
      if (aceptar) {
        this.spin = true;
        this.serviceColectivo
          .disableColectivo(this.colectivo.id)
          .subscribe((resultOk) => {
            this.spin = false;
            this._snackbar.open(
              resultOk
                ? "Colectivo " + this.colectivo.unidad + " se dio de baja"
                : "No se pudo dar de baja Colectivo " + this.colectivo.unidad,
              "",
              {
                duration: 4500,
                verticalPosition: "top", // 'top' | 'bottom'
                horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
                panelClass: resultOk ? ["blue-snackbar"] : ["red-snackbar"],
              }
            );
            if (resultOk)
              this.router.navigate(["../.."], { relativeTo: this.route });
          });
      }
    });
  }

  onImagenSelect(event: any) {
    if (event.target.files && event.target.files[0]) {
      const file = event.target.files[0];
      const ext = file.name.substring(file.name.lastIndexOf(".")).toLowerCase();
      if (ext != ".jpg" && ext != ".jpeg" && ext != ".png") {
        this._snackbar.open("Formato de archivo no permitido", "", {
          duration: 4500,
          verticalPosition: "bottom", // 'top' | 'bottom'
          horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
          panelClass: ["red-snackbar"],
        });
      } else {
        this.imagen = file;
        const blob = new Blob([file], { type: file.type });
        this.url = this.sanitizer.bypassSecurityTrustResourceUrl(
          window.URL.createObjectURL(blob)
        );
      }
    }
  }
}
