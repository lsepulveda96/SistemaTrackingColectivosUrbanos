export interface Colectivo {
    id: number;
    unidad: string;
    patente: string;
    marca: string;
    modelo: string;
    anio: number;
    capacidad: number;
    fechaCompra: Date;
    estado: string;
    fechaBaja: Date;
    enCirculacion: boolean;
    imgpath: string;
    documentos: any[];
}
