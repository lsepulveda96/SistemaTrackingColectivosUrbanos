import { Linea } from "./linea";

export interface Recorrido {

    id: number;
    fechaInicio: Date;
    fechaFin: Date;
    activo: boolean;
    Linea: Linea;
}