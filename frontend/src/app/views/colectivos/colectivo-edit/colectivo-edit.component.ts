import { Component, OnInit } from "@angular/core";
import { FormControl, UntypedFormControl, Validators } from "@angular/forms";
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
  opening: boolean;
  id: any;
  colectivo: Colectivo | undefined;

  unidadIC = new FormControl("", Validators.required);
  patenteIC = new FormControl("", [
    Validators.required,
    Validators.pattern("^([A-Z]{2}[0-9]{3}[A-Z]{2})$|^([A-Z]{3}[0-9]{3})$")
  ]);
  marcaIC = new FormControl("", Validators.required);
  modeloIC = new FormControl("");
  anioIC = new FormControl(
    null,
    Validators.pattern("^[12]{1}[09]{1}[0-9]{2}$")
  ); // 19xx o 20xx
  capacidadIC = new FormControl(
    null,
    Validators.pattern("^[1-9][0-9]?$")
  ); // numero entre 1 y 100
  compraIC = new FormControl(null);

  estados: string[] = ["HABILITADO", "NO HABILITADO"];
  estadoIC = new FormControl("");

  imagen: any;
  url: any;

  documentos: any[];
  editDoc: boolean;
  docNombreIC = new FormControl('', Validators.required);
  docVenceIC = new FormControl(false);
  docVencimientoIC = new FormControl(null);
  docNameFile = '';
  docFile: any;
  indexEdit: number;

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
    this.documentos = [];
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
        this.documentos = this.colectivo.documentos.map((doc: any) => {
          doc.vencimiento = new Date(doc.vencimiento)
          return doc;
        });
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

  /**
   * Registra nueva unidad colectivo, carga su imagen(si la tiene) y sus documentos (si los tiene),
   */
  guardarColectivo() {
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
      imgpath: null,
      documentos: [],
    };
    this.spin = true;
    this.serviceColectivo.saveColectivo(this.colectivo).subscribe(result => {
      this.spin = false;
      this._snackbar.open(result.mensaje, result.error ? 'Error' : 'Exito', {
        duration: 4500,
        verticalPosition: "bottom", // 'top' | 'bottom'
        horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
        panelClass: result.error ? ["red-snackbar"] : ["blue-snackbar"],
      });
      if (!result.error) {
        const idColectivo = result.data.id;
        if (this.imagen) { // si hay imagen se sube.
          this.spin = true;
          this.serviceColectivo.uploadImagen(idColectivo, this.imagen).subscribe(
            resultUploadImg => {
              this.spin = false;
              if (resultUploadImg.error) {
                this._snackbar.open(result.mensaje, "Error imagen", {
                  duration: 4500,
                  verticalPosition: "bottom", // 'top' | 'bottom'
                  horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
                  panelClass: ["red-snackbar"],
                });
              }
            },
            err => {
              this.spin = false;
              if (err.error) {
                this._snackbar.open(result.mensaje, "Error imagen", {
                  duration: 4500,
                  verticalPosition: "bottom", // 'top' | 'bottom'
                  horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
                  panelClass: ["red-snackbar"],
                });
              }
            }
          );
        }
        for (let doc of this.documentos) { // se suben los documentos.
          this.spin = true;
          this.serviceColectivo.uploadDoc(idColectivo, doc.nombre, doc.vence, doc.vencimiento, doc.file)
            .subscribe(
              resultUploadDoc => {
                this.spin = false;
                if (resultUploadDoc.error) {
                  this._snackbar.open(result.mensaje, "Error documento", {
                    duration: 4500,
                    verticalPosition: "bottom", // 'top' | 'bottom'
                    horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
                    panelClass: ["red-snackbar"],
                  });
                }
              },
              err => {
                this.spin = false;
                if (err.error) {
                  this._snackbar.open(result.mensaje, "Error documento", {
                    duration: 4500,
                    verticalPosition: "bottom", // 'top' | 'bottom'
                    horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
                    panelClass: ["red-snackbar"],
                  });
                }
              }
            );
        }
        this.router.navigate(["../"], { relativeTo: this.route });
      }
    });
  }


  actualizarColectivo() {
    this.colectivo.unidad = this.unidadIC.value.toUpperCase();
    this.colectivo.marca = this.marcaIC.value.toUpperCase();
    this.colectivo.modelo = this.modeloIC.value.toUpperCase();
    this.colectivo.patente = this.patenteIC.value.toUpperCase();
    this.colectivo.anio = this.anioIC.value;
    this.colectivo.capacidad = this.capacidadIC.value;
    this.colectivo.fechaCompra = this.compraIC.value;
    this.spin = true;
    this.serviceColectivo.updateColectivo(this.colectivo).subscribe(result => {
      this.spin = false;
      this._snackbar.open(result.mensaje, result.error ? 'Error' : 'Exito', {
        duration: 4500,
        verticalPosition: "bottom", // 'top' | 'bottom'
        horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
        panelClass: result.error ? ["red-snackbar"] : ["blue-snackbar"],
      });
      if (!result.error) {
        if (this.imagen) { // si se actualizo la imagen se carga nuevamente.
          this.spin = true;
          this.serviceColectivo.uploadImagen(this.colectivo.id, this.imagen)
            .subscribe(
              resultUploadImg => {
                this.spin = false;
                if (resultUploadImg.error) {
                  this._snackbar.open(result.mensaje, "Error imagen", {
                    duration: 4500,
                    verticalPosition: "bottom", // 'top' | 'bottom'
                    horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
                    panelClass: ["red-snackbar"],
                  });
                }
              },
              err => {
                this.spin = false;
                if (err.error) {
                  this._snackbar.open(result.mensaje, "Error imagen", {
                    duration: 4500,
                    verticalPosition: "bottom", // 'top' | 'bottom'
                    horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
                    panelClass: ["red-snackbar"],
                  });
                }
              });
        }
        const addDocs = this.documentos.filter((doc: any) => !doc.id); // documentos nuevos a cargar.
        const updDocs = this.documentos.filter((doc: any) => doc.id) // documentos a actualizar.
        const remDocs = this.colectivo.documentos.filter((doc: any) => !this.documentos.find((d: any) => doc.id == d.id)); // documentos a eliminar.

        for (let doc of addDocs) {
          this.spin = true;
          this.serviceColectivo.uploadDoc(this.colectivo.id, doc.nombre, doc.vence, doc.vencimiento, doc.file)
            .subscribe(resultUploadDoc => {
              this.spin = false;
              if (resultUploadDoc.error) {
                this._snackbar.open(result.mensaje, "Error documento", {
                  duration: 4500,
                  verticalPosition: "bottom", // 'top' | 'bottom'
                  horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
                  panelClass: ["red-snackbar"],
                });
              }
            },
              err => {
                this.spin = false;
                if (err.error) {
                  this._snackbar.open(result.mensaje, "Error documento", {
                    duration: 4500,
                    verticalPosition: "bottom", // 'top' | 'bottom'
                    horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
                    panelClass: ["red-snackbar"],
                  });
                }
              });
        }

        for (let doc of updDocs) {
          if (doc.file) { // actualiza datos y archivo.
            this.serviceColectivo.updateDocFile(doc.id, doc.nombre, doc.vence, new Date(doc.vencimiento), doc.file)
              .subscribe(resultUpdDoc => {
                if (resultUpdDoc.error) {
                  this._snackbar.open(result.mensaje, "Error actualizando documento", {
                    duration: 4500,
                    verticalPosition: "bottom", // 'top' | 'bottom'
                    horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
                    panelClass: ["red-snackbar"],
                  });
                }
              },
                err => {
                  if (err.error) {
                    this._snackbar.open(result.mensaje, "Error actualizando documento", {
                      duration: 4500,
                      verticalPosition: "bottom", // 'top' | 'bottom'
                      horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
                      panelClass: ["red-snackbar"],
                    });
                  }
                });
          }
          else { // Actualiza solo datos.
            this.serviceColectivo.updateDocData(doc.id, doc )
              .subscribe(resultUpdDoc => {
                if (resultUpdDoc.error) {
                  this._snackbar.open(result.mensaje, "Error actualizando documento", {
                    duration: 4500,
                    verticalPosition: "bottom", // 'top' | 'bottom'
                    horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
                    panelClass: ["red-snackbar"],
                  });
                }
              });
          }
        }

        for (let doc of remDocs) {
          this.spin = true;
          this.serviceColectivo.deleteDoc(doc.id).subscribe(resultDelDoc => {
            this.spin = false;
            if (resultDelDoc.error) {
              this._snackbar.open(result.mensaje, "Error eliminado documento", {
                duration: 4500,
                verticalPosition: "bottom", // 'top' | 'bottom'
                horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
                panelClass: ["red-snackbar"],
              });
            }
          },
            err => {
              this.spin = false;
              if (err.error) {
                this._snackbar.open(result.mensaje, "Error eliminado documento", {
                  duration: 4500,
                  verticalPosition: "bottom", // 'top' | 'bottom'
                  horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
                  panelClass: ["red-snackbar"],
                });
              }
            });
        }
        this.router.navigate(["../.."], { relativeTo: this.route });
      }
    });
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

  getClass(name: string) {
    const ext = name.split('.').pop();
    if (ext == 'pdf')
      return 'bi bi-file-pdf-fill text-danger';
    else if (ext == 'jpg' || ext == 'jpeg' || ext == 'png')
      return 'bi bi-file-image-fill text-primary';
    return 'bi bi-file-earmark-fill text-secondary';
  }

  onDocSelect(event: any) {
    if (event.target.files) {
      this.docFile = event.target.files[0];
      this.docNameFile = this.docFile.name;
    }
  }

  openDocument(doc: any) {
    if (!doc.id) {
      const blob = new Blob([doc.file]);
      window.open(window.URL.createObjectURL(blob));
    }
    else {
      this.opening = true;
      this.serviceColectivo.downloadDoc( doc.pathfile ).subscribe( data =>{
        this.opening = false;
        if (!data.error) {
          const blob = new Blob([data]);
          window.open( window.URL.createObjectURL(blob)); 
        }
      }); 
    }
  }

  nuevaDocumentacion() {
    this.docNombreIC.setValue('');
    this.docNameFile = '';
    this.docFile = null;
    this.docVenceIC.setValue(false);
    this.docVencimientoIC.setValue(null);
    this.editDoc = true;
    this.indexEdit = -1;
  }

  editarDocumentacion(index: number) {
    this.indexEdit = index;
    const doc = this.documentos[index];
    console.log("editar doc: ", doc);
    this.docNombreIC.setValue(doc.nombre);
    this.docNameFile = doc.namefile;
    this.docVenceIC.setValue(doc.vence);
    this.docVencimientoIC.setValue(doc.vencimiento);
    this.docFile = doc.file;
    this.editDoc = true;
  }

  cerrarEditarDoc() {
    this.editDoc = false;
  }

  guardarDocumentacion() {
    if (this.documentos.find((doc: any) => doc.nombre.toUpperCase() == this.docNombreIC.value.toUpperCase())) {
      this._snackbar.open('Ya existe documento con igual nombre', '', {
        duration: 4500,
        verticalPosition: "bottom", // 'top' | 'bottom'
        horizontalPosition: "end", //'start' | 'center' | 'end' | 'left' | 'right'
        panelClass: ["red-snackbar"],
      })
      return;
    }
    const newDoc = {
      id: null,
      namefile: this.docNameFile,
      nombre: this.docNombreIC.value,
      vence: this.docVenceIC.value,
      vencimiento: this.docVencimientoIC.value,
      file: this.docFile
    }
    this.documentos.push(newDoc);
    this.cerrarEditarDoc();
  }

  actualizarDocumentacion() {
    this.documentos[this.indexEdit].nombre = this.docNombreIC.value;
    this.documentos[this.indexEdit].nameFile = this.docNameFile;
    this.documentos[this.indexEdit].file = this.docFile;
    this.documentos[this.indexEdit].vence = this.docVenceIC.value;
    this.documentos[this.indexEdit].vencimiento = this.docVencimientoIC.value;
    this.cerrarEditarDoc();
  }

  quitarDocumento(index: number) {
    const doc = this.documentos[index];
    const data = { titulo: 'Quitar documento', mensaje: 'Confirma quitar documento ' + doc.nombre + '?' };
    const refDialog = this.dialog.open(ConfirmComponent, { data: data });
    refDialog.afterClosed().subscribe(aceptar => {
      console.log("aceptar: ", aceptar);
      if (aceptar) {
        this.documentos = this.documentos.filter(d => d.nombre != doc.nombre);
        console.log("docs: ", this.documentos);
      }
    });
  }
}
