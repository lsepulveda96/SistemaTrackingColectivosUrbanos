package com.stcu.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stcu.dto.response.ColectivoRecorridoDTO;
import com.stcu.dto.response.CoordenadaDTO;
import com.stcu.dto.response.NotificacionDTO;
import com.stcu.model.ColectivoRecorrido;
import com.stcu.model.Notificacion;
import com.stcu.model.Ubicacion;
import com.stcu.services.MonitorService;

@RestController
@RequestMapping("/api")
public class MonitorController {

    @Autowired
    private MonitorService service;

    private static final Logger log = Logger.getLogger(MonitorController.class.getName());

    /**
     * Busca la lista de colectivos en trasito en el momento.
     * 
     * @return
     */
    @GetMapping("/transito/unidades")
    public String findColectivosTransito() {
        List<ColectivoRecorrido> list = service.getColectivosTransito();
        log.info("*** Colectivos en transito: " + list.size());
        Response<List<ColectivoRecorridoDTO>> response = new Response<List<ColectivoRecorridoDTO>>(false, 200,
                "Colectivo en Recorrido ", ColectivoRecorridoDTO.toListColectivoRecorridoDTO(list));
        return Mapper.getResponseAsJson(response);
    }

    /**
     * Recupera info de una unidad en recorrido a partir de su id de transito
     * 
     * @param id
     * @return
     */
    @GetMapping("/transito/unidad/{id}")
    public String findColectivoRecorridoTransito(@PathVariable long id) {
        log.info("*** Colectivo recorrido en transito: " + id);
        ColectivoRecorrido colRec = this.service.getColectivoRecorrido(id);
        List<Ubicacion> ubicaciones;
        Response<ColectivoRecorridoDTO> response;
        if (colRec != null) { // Si existe el colectivo recorrido recupera todas las coordenadas registradas hasta el momento
            ubicaciones = this.service.findUbicaciones(id);
            List<CoordenadaDTO> coordenadas = new ArrayList<CoordenadaDTO>();
            for (Ubicacion ubicacion: ubicaciones) {
                CoordenadaDTO coor = new CoordenadaDTO( ubicacion.getCoordenada().getX(), ubicacion.getCoordenada().getY() );
                coordenadas.add(coor);
            }
            ColectivoRecorridoDTO crDto = new ColectivoRecorridoDTO( colRec );
            crDto.setCoordenadas(coordenadas);

            response = new Response<ColectivoRecorridoDTO>(false, 200, "Colectivo recorrido",
                    crDto);
        }
        else
            response = new Response<ColectivoRecorridoDTO>(true, 400, "No se encontro colectivo recorrido", null);
        log.info("*** Colectivos en transito response: " + response.getMensaje());
        return Mapper.getResponseAsJson(response);
    }

    /**
     * Recupera ultima coordenada de una unidad en recorrido.
     * 
     * @param id
     * @return
     */
    @GetMapping("/transito/coordenadas/unidad/{id}")
    public String getCoordenadasColectivosTransito(@PathVariable long id) {
        log.info("*** Coordenadas colectivo en transito: " + id);
        Ubicacion coordColeRec = this.service.getLastUbicacion(id);
        Response<CoordenadaDTO> response;
        if (coordColeRec != null)
            response = new Response<CoordenadaDTO>(false, 200, "Coordenada colectivo recorrido",
                    new CoordenadaDTO(coordColeRec.getCoordenada().getX(), coordColeRec.getCoordenada().getY()));
        else
            response = new Response<CoordenadaDTO>(true, 400, "No se encontraron coordenadas colectivo recorrido",
                    null);
        log.info("*** Coordenadas colectivo en transito response: " + response.getMensaje());
        return Mapper.getResponseAsJson(response);
    }

    @DeleteMapping("/transito/detener/unidad/{idColRec}/{idLinea}/{disabled}")
    public String detenerColectivoRecorrido(@PathVariable long idColRec, @PathVariable long idLinea,
            @PathVariable boolean disabled) {

        // continuar aca, reemplazar por valores de detener colectivo rec
        ColectivoRecorrido colRec = service.detenerColectivoRecorrido(idColRec, idLinea, disabled);
        Response<ColectivoRecorridoDTO> response;
        if (colRec != null) {
            response = new Response<ColectivoRecorridoDTO>(false, 200, "Colectivo-Recorrido " + idColRec + " detenido",
                    null);
        } else {
            response = new Response<ColectivoRecorridoDTO>(true, 400, "No se pudo detener unidad " + idColRec, null);
        }
        return Mapper.getResponseAsJson(response);
    }

    // para pantalla notificaciones activas
    @GetMapping("/notificacion/activas")
    public String findNotificacionesActivas() {
        List<Notificacion> list = service.findNotificacionesActivas();
        Response<List<NotificacionDTO>> response = new Response<List<NotificacionDTO>>(false, 200,
                "Notificaciones activas ", NotificacionDTO.toListNotificacionDTO(list));
        return Mapper.getResponseAsJson(response);
    }
}
