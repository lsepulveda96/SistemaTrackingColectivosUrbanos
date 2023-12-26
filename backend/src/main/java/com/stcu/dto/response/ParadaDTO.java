package com.stcu.dto.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.stcu.model.Parada;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParadaDTO implements Serializable {

    private long codigo;

    private String direccion;

    private String descripcion;

    private String estado;

    private CoordenadaDTO coordenada;

    public ParadaDTO() {
    }

    public ParadaDTO(Parada par) {
        codigo = par.getCodigo();
        direccion = par.getDireccion();
        descripcion = par.getDescripcion();
        estado = par.getEstado();

        Point point = par.getCoordenadas();
        if (point != null)
            coordenada = new CoordenadaDTO(point.getX(), point.getY());
    }

    /**
     * Convierte el objeto actual paradaDTO en un objeto Parada
     * 
     * @return parada
     */
    public Parada ToParada() {
        Parada parada = new Parada();
        parada.setCodigo(codigo);
        parada.setDireccion(direccion);
        parada.setDescripcion(descripcion);
        parada.setEstado(estado);

        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(coordenada.getLat(), coordenada.getLng()));
        parada.setCoordenadas(point);

        return parada;
    }

    /**
     * Convierte una lista de objetos Parada a una lista de objetos ParadaDTO
     * 
     * @param paradas
     * @return
     */
    public static List<ParadaDTO> toListParadaDTO(List<Parada> paradas) {
        List<ParadaDTO> paradasDto = new ArrayList<ParadaDTO>();
        paradas.forEach(par -> {
            paradasDto.add(toDTO(par));
        });
        return paradasDto;
    }

    /**
     * Convierte un objeto de tipo Parada a un objeto de tipo ParadaDTO.
     * 
     * @param parada
     * @return
     */
    private static ParadaDTO toDTO(Parada parada) {
        ParadaDTO paradaDto = new ParadaDTO();
        paradaDto.setCodigo(parada.getCodigo());
        paradaDto.setDireccion(parada.getDireccion());
        paradaDto.setDescripcion(parada.getDescripcion());
        paradaDto.setEstado(parada.getEstado());

        Point point = parada.getCoordenadas();
        paradaDto.setCoordenada(new CoordenadaDTO(point.getX(), point.getY()));

        return paradaDto;
    }

}
