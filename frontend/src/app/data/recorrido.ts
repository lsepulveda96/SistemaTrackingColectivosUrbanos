import { Coordenada } from "./coordenada";
import { Linea } from "./linea";

export interface Recorrido {

    id: number;
    fechaInicio: Date;
    fechaFin: Date;
    activo: boolean;
    Linea: Linea;

    trayectos: Coordenada[];
    waypoints: Coordenada[];
}