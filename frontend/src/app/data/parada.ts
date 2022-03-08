import { Coordenada } from "./coordenada";

export interface Parada {
    codigo: number;
    direccion: string;
    descripcion: string;
    estado: string;
    coordenada: Coordenada;
}
