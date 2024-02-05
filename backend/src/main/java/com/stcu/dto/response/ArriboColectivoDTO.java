package com.stcu.dto.response;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.stcu.model.Colectivo;
import com.stcu.model.ColectivoRecorrido;
import com.stcu.model.Parada;
import com.stcu.model.Recorrido;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArriboColectivoDTO {

    Calendar fechaParadaActual;
    String tiempoArriboColProximo;
    Parada paradaActual;
    Parada paradaActualPasajero;

    public ArriboColectivoDTO() {
    }

    public ArriboColectivoDTO(Calendar fechaParadaActual, String tiempoArriboColProximo, Parada paradaActual, Parada paradaActualPasajero ) {
        this.fechaParadaActual = fechaParadaActual;
        this.tiempoArriboColProximo = tiempoArriboColProximo;
        Parada parActCole = paradaActual;
        if (parActCole != null) {
            this.paradaActual = new Parada(parActCole.getCodigo(), parActCole.getDireccion(),
                    parActCole.getCoordenadas());
        }
        Parada parActPasajero = paradaActualPasajero;
        if (parActPasajero != null) {
            this.paradaActualPasajero = new Parada(parActPasajero.getCodigo(), parActPasajero.getDireccion(),
                    parActPasajero.getCoordenadas());
        }
    }

    public static ArriboColectivoDTO toColectivoRecorridoDTO(ArriboColectivoDTO acDTO) {
        ArriboColectivoDTO nacDTO = new ArriboColectivoDTO();

        nacDTO.setFechaParadaActual(acDTO.getFechaParadaActual());
        nacDTO.setTiempoArriboColProximo(acDTO.getTiempoArriboColProximo());
        Parada parActColectivo = acDTO.getParadaActual();
        if (parActColectivo != null) {
            nacDTO.setParadaActual(new Parada(parActColectivo.getCodigo(), parActColectivo.getDireccion(),
                    parActColectivo.getCoordenadas()));
        }
        Parada parActPasajero = acDTO.getParadaActualPasajero();
        if (parActPasajero != null) {
            nacDTO.setParadaActualPasajero(new Parada(parActPasajero.getCodigo(), parActPasajero.getDireccion(),
                    parActPasajero.getCoordenadas()));
        }

        return nacDTO;

    }

}
