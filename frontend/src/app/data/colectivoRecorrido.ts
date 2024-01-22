import { Colectivo } from "./colectivo";
import { Coordenada } from "./coordenada";

export interface ColectivoRecorrido {
    id: number;
    unidad: number;
    colectivo: Colectivo;
    transito: boolean;
    coordenadas: Coordenada[];
}
