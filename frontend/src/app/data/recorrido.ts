import { Coordenada } from "./coordenada";
import { Linea } from "./linea";

export interface Recorrido {

    id: number;
    denominacion: string;
    fechaInicio: Date;
    fechaFin: Date;
    activo: boolean;
    linea: Linea;
    
    trayectos: Coordenada[]; // Trayectos del recorrido creados por el router.
    waypoints: Coordenada[]; // waypoints del router entre paradas.
    paradas: any[]; // lista de paradas en el recorrid
}