import { Colectivo } from "./colectivo";
import { Coordenada } from "./coordenada";

export interface ColectivoRecorrido {
    id: number;
    colectivo: Colectivo;
    transito: boolean;
    coordenadas: Coordenada[];
    recorridoId: number;
    recorridoDenominacion: string;
    lineaId: number; 
    lineaDenominacion: string;
}
