import { Coordenada } from "./coordenada";
import { Linea } from "./linea";

export interface Recorrido {

    id: number;
    denominacion: String;
    fechaInicio: Date;
    fechaFin: Date;
    activo: boolean;
    linea: Linea;
    
    trayectos: Coordenada[]; // Trayectos del recorrido creados por el router.
    waypoints: Coordenada[]; // waypoints del router entre paradas.
}