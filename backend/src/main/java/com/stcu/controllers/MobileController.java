package com.stcu.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.json.JSONException;
import org.json.JSONObject;
import com.stcu.dto.response.ColectivoRecorridoDTO;
import com.stcu.dto.response.NotificacionDTO;
import com.stcu.dto.response.ParadaRecorridoDTO;
import com.stcu.dto.response.RecorridoDTO;
import com.stcu.model.Colectivo;
import com.stcu.model.ColectivoRecorrido;
import com.stcu.model.Linea;
import com.stcu.model.Notificacion;
import com.stcu.model.ParadaRecorrido;
import com.stcu.model.Recorrido;
import com.stcu.services.ColectivoServiceImp;
import com.stcu.services.LineaServiceImp;
import com.stcu.services.MonitorServiceImp;
import com.stcu.services.ParadaServiceImp;
import com.stcu.services.RecorridoServiceImp;

@RestController
@RequestMapping("/v1/mobile")
public class MobileController {

    private static final Logger log = Logger.getLogger(MobileController.class.getName());

    @Autowired
    LineaServiceImp serviceLinea;

    @GetMapping("/test")
    public String testMobileApi() {
        log.info("*** testMobileApi ");
        return "Conectado";
    }
    
    @Autowired
    ParadaServiceImp serviceParada;
  
    @Autowired
    RecorridoServiceImp serviceRecorrido;
  
    @Autowired
    ColectivoServiceImp serviceColectivo;
  
    @Autowired
    MonitorServiceImp serviceMonitor;
  
    static String consulta = "null";
  
    /**
     * Recupera todas las lineas activas disponibles
     * 
     * @return lista de lineas activas
     */
    @GetMapping("/lineas/activas")
    public String findAllLineas() {
      List<Linea> lineasActivas = serviceLinea.getLineasActivas();
      Response<List<Linea>> response = new Response<List<Linea>>(false, 200, "Lineas", lineasActivas);
      return Mapper.getResponseAsJson(response);
    }
  
    /**
     * Recupera lista de colectivos activos fuera de circulacion
     * 
     * @return colectivos fuera de circulacion
     */
    @GetMapping("/colectivos")
    public String findAllColectivos() {
      List<Colectivo> colectivos = serviceColectivo.getAllColectivosSinCircular();
      Response<List<Colectivo>> response = new Response<List<Colectivo>>(false, 200, "Colectivos", colectivos);
      return Mapper.getResponseAsJson(response);
    }
  
    /**
     * Recupera las paradas de un recorrido especifico de una linea
     * 
     * @param lineaDenom     denominacion de la linea
     * @param recorridoDenom denominacion del recorrido
     * @return lista de paradasRecorrido de un recorrido-linea
     */
    @GetMapping("/paradasParaApp/{lineaDenom}/{recorridoDenom}")
    public String findParadasRecorrido(@PathVariable String lineaDenom, @PathVariable String recorridoDenom) {
      List<ParadaRecorrido> prs = serviceRecorrido.getParadasRecorridoByLineaDenomYRecorridoDenom(lineaDenom,
          recorridoDenom);
      Response<List<ParadaRecorridoDTO>> response;
      if (prs != null)
        response = new Response<List<ParadaRecorridoDTO>>(false, 200, "Paradas de recorrido ",
            ParadaRecorridoDTO.toListParadaRecorridoDTO(prs));
      else
        response = new Response<List<ParadaRecorridoDTO>>(true, 400,
            "No se pudo recuperar lista de paradas de recorrido ", null);
      return Mapper.getResponseAsJson(response);
    }
  
    /**
     * Recupera las paradas de todos los recorrido activos de una linea
     * 
     * @param lineaDenom denominacion de la linea
     * @return lista de paradasRecorrido de todos los recorridos activos de una
     *         linea
     */
    @GetMapping("/findAllParadasParaApp/{lineaDenom}")
    public String findAllParadasRecorrido(@PathVariable String lineaDenom) {
      List<ParadaRecorrido> prs = serviceRecorrido.getParadasRecorridoByLineaDenom(lineaDenom);
      Response<List<ParadaRecorridoDTO>> response;
      if (prs != null)
        response = new Response<List<ParadaRecorridoDTO>>(false, 200, "Paradas de recorrido ",
            ParadaRecorridoDTO.toListParadaRecorridoDTO(prs));
      else
        response = new Response<List<ParadaRecorridoDTO>>(true, 400,
            "No se pudo recuperar lista de paradas de recorrido ", null);
      return Mapper.getResponseAsJson(response);
    }
  
    @GetMapping("/recorridosActivos/{denomLinea}")
    public String findRecorridoActivos(@PathVariable String denomLinea) {
      List<Recorrido> recorridosActivos = serviceRecorrido.getRecorridosActivosByDenomLinea(denomLinea);
      Response<List<RecorridoDTO>> response = new Response<List<RecorridoDTO>>(false, 200,
          "RecorridosActivos" + denomLinea, RecorridoDTO.toListRecorridosDTO(recorridosActivos));
      return Mapper.getResponseAsJson(response);
    }
  
    /**
     * Busca el trayecto a recorrer y sus paradas correspondientes
     * 
     * @param denomLinea     denominacion linea
     * @param denomRecorrido denominacion recorrido
     * @return lista de paradas a recorrer
     */
    @GetMapping("/trayectos/{denomLinea}/{denomRecorrido}")
    public String findTrayectosRecorrido(@PathVariable String denomLinea, @PathVariable String denomRecorrido)
        throws IOException {
  
      Recorrido recorrido = serviceRecorrido.getRecorridoByLineaDenomYRecorridoDenom(denomLinea, denomRecorrido);
      // trae trayecto a recorrer
      List<ParadaRecorrido> paradas = serviceRecorrido.getParadasRecorrido(recorrido.getId());
      Response<List<ParadaRecorridoDTO>> response;
  
      if (paradas != null)
        response = new Response<List<ParadaRecorridoDTO>>(false, 200, "Paradas de recorrido " + recorrido.getId(),
            ParadaRecorridoDTO.toListParadaRecorridoDTO(paradas));
      else
        response = new Response<List<ParadaRecorridoDTO>>(true, 400,
            "No se pudo recuperar lista de paradas de recorrido " + recorrido.getId(), null);
      return Mapper.getResponseAsJson(response);
    }
  
    @PostMapping("/inicio")
    public String inicioServicio(@RequestBody String json) {
  
      String lineaDenom = "";
      String unidad = "";
      String recorridoDenom = "";
      String longitud = "";
      String latitud = "";
  
      Response<ColectivoRecorridoDTO> response;
      consulta = json;
  
      // prueba para traer json object
      try {
        JSONObject obj = new JSONObject(json);
  
        lineaDenom = obj.getString("linea");
        unidad = obj.getString("colectivo");
        recorridoDenom = obj.getString("recorrido");
        longitud = obj.getString("longitud");
        latitud = obj.getString("latitud");
  
      } catch (JSONException e) {
        e.printStackTrace();
      }
  
      Colectivo colectivo = serviceColectivo.getColectivoByUnidad(unidad);
      Recorrido recorrido = serviceRecorrido.getRecorridoByLineaDenomYRecorridoDenom(lineaDenom, recorridoDenom);
  
      ColectivoRecorrido colectivoRecorrido = serviceMonitor
          .saveColectivoRecorrido(new ColectivoRecorrido(colectivo, recorrido));
  
      if (colectivoRecorrido != null) {
        response = new Response<ColectivoRecorridoDTO>(false, 200, "Servicio iniciado",
            ColectivoRecorridoDTO.toColectivoRecorridoDTO(colectivoRecorrido));
  
        serviceMonitor.createUbicacion(colectivoRecorrido, latitud, longitud);
  
      } else {
        response = new Response<ColectivoRecorridoDTO>(true, 200, "La unidad se encuentra en servicio actualmente", null);
      }
      return Mapper.getResponseAsJson(response);
    }
  
    @PostMapping("/coleEnParada")
    public String coleEnParada(@RequestBody String json) {
  
      String denomLinea = "", denomRecorrido = "", unidad = "", codigoParada = "";
      Response<ColectivoRecorridoDTO> response;
  
      try {
        JSONObject obj = new JSONObject(json);
  
        denomLinea = obj.getString("linea");
        denomRecorrido = obj.getString("recorrido");
        unidad = obj.getString("colectivo");
        codigoParada = obj.getString("codigo");
      } catch (JSONException e) {
        e.printStackTrace();
      }
  
      System.out.println("++++++++++++++++++++ cole en parada ");
      System.out.println("Colectivo: " + unidad + " Linea: " + denomLinea + " CodParada: " + codigoParada);
  
      // lineaColectivoService.updateParadaActual(denom,unidad,codigo);
      ColectivoRecorrido cr = serviceMonitor.updateParadaActual(denomLinea, denomRecorrido, unidad,
          Long.parseLong(codigoParada));
  
      if (cr != null) {
        response = new Response<ColectivoRecorridoDTO>(false, 200, "Colectivo recorrido actualizado",
            ColectivoRecorridoDTO.toColectivoRecorridoDTO(cr));
      } else {
        response = new Response<ColectivoRecorridoDTO>(true, 200, "Error al actualizar Colectivo recorrido", null);
      }
      return Mapper.getResponseAsJson(response);
    }
  
    /**
     * Metodo que verifica si un colectivo esta desviado de su trayecto habitual, si
     * lo esta
     * envia una notificacion de desvio al servidor
     * 
     * @param json datos enviados desde app colectivo. Contiene nombres de linea,
     *             recorrido, colectivo, y ubicacion en tiempo real
     * @return
     */
    @PostMapping("/detectarDesvio")
    public String detectarDesvio(@RequestBody String json) {
      Response<NotificacionDTO> response;
      String denomLinea = "", denomRecorrido = "", unidad = "", latitud = "", longitud = "";
  
      try {
        JSONObject obj = new JSONObject(json);
        denomLinea = obj.getString("linea");
        unidad = obj.getString("colectivo");
        denomRecorrido = obj.getString("recorrido");
        latitud = obj.getString("latitud");
        longitud = obj.getString("longitud");
      } catch (JSONException e) {
        e.printStackTrace();
      }
  
      System.out.println("parametros ENVIO DESVIO ++++++++++++++++ " + denomLinea + " " + unidad + " " + denomRecorrido
          + " " + latitud + " " + longitud);
      ColectivoRecorrido cr = serviceMonitor.getColectivoRecorridoByLinDenomRecDenomColUnidad(denomLinea, denomRecorrido,
          unidad);
      // si devuelve true, esta en el recorrido
      Recorrido recorridoAct = serviceRecorrido.getRecorridoByLineaDenomYRecorridoDenom(denomLinea, denomRecorrido);
      Boolean enRecorrido = serviceRecorrido.verificarUnidadEnRecorrido(latitud, longitud, recorridoAct.getId());
      System.out.println(" ++++++++++++++++ el resultado de verificar unidad en recorrido +++++++++ " + enRecorrido);
      // si no esta en recorrido
      Notificacion respNotificacion = new Notificacion();
      if (!enRecorrido) {
        try{ // ver que parametro usar para atrapar el error
        respNotificacion = serviceMonitor.notificacionDesvio(cr, latitud, longitud, true);
        System.out.println(" ++++++++++++++++ notificacion enviada, el colectivo esta fuera del trayecto");
        response = new Response<NotificacionDTO>(false, 200,
            "Notificacion enviada, el colectivo esta fuera del trayecto!",
            NotificacionDTO.toDTO(respNotificacion));
        }catch(NullPointerException e){ // si da error por colectivo nulo
          response = new Response<NotificacionDTO>(true, 400,
            "Error al enviar notificacion!",
            null);
        }
      } else {
        // si esta en recorrido, verifica que no haya estado desviado, si lo esta setea
        // colectivo en false
        Boolean colectivoEstaDesviado = serviceMonitor.desvioActivo(cr.getId());
        System.out.println(" ++++++++++++++++ Habia desvio activo? " + colectivoEstaDesviado);
        if (colectivoEstaDesviado) {
          respNotificacion = serviceMonitor.finNotificacionDesvio(cr.getId(), false);
          System.out.println(" ++++++++++++++++ notificacion desactivada, el colectivo retomo el trayecto habitual");
          response = new Response<NotificacionDTO>(false, 200,
              "El colectivo estaba desviado, retomo el trayecto habitual",
              NotificacionDTO.toDTO(respNotificacion));
        } else {
          System.out.println(" ++++++++++++++++ el colectivo esta dentro del trayecto habitual");
          response = new Response<NotificacionDTO>(false, 200, "El colectivo esta dentro del trayecto habitual",
              null);
        }
      }
      return Mapper.getResponseAsJson(response);
    }
  
    /**
     * Metodo que utiliza app colectivo para detener un colectivo en servicio
     * 
     * @param json datos enviados desde la app colectivo para detener el servicio
     * @return respuesta indicando si se pudo detener el servicio correctamente
     */
    @PostMapping("/fin")
    public String finServicio(@RequestBody String json) {
  
      //set transito false, no lo da de baja
      Response<ColectivoRecorridoDTO> response;
      String denomLinea = "", denomRecorrido = "", unidad = "";
  
      try {
        JSONObject obj = new JSONObject(json);
  
        denomLinea = obj.getString("linea");
        unidad = obj.getString("colectivo");
        denomRecorrido = obj.getString("recorrido");
      } catch (JSONException e) {
        e.printStackTrace();
      }
  
      boolean enTransito = false;
      // solo se detiene, en transito false. hacerlo con ids y no con denom
      //ColectivoRecorrido cr = serviceMonitor.bajaColectivoRecorrido(denomLinea, unidad, denomRecorrido, Calendar.getInstance(), enTransito);
      ColectivoRecorrido cr = serviceMonitor.detenerColectivoRecorridoByDenom(denomLinea, unidad, denomRecorrido, enTransito);
  
      if (cr == null) { 
        response = new Response<ColectivoRecorridoDTO>(true, 200, "La unidad no se encuentra en servicio",
            ColectivoRecorridoDTO.toColectivoRecorridoDTO(cr));
      } else {
        response = new Response<ColectivoRecorridoDTO>(false, 200, "Servicio finalizado",
            ColectivoRecorridoDTO.toColectivoRecorridoDTO(cr));
      }
      return Mapper.getResponseAsJson(response);
    }
  
    /**
     * Metodo que utiliza app colectivo para indicar que la notificacion de desvio
     * termino
     * 
     * @param json datos enviados desde app colectivo. Contiene nombres de linea,
     *             colectivo y recorrido
     * @return respuesta indicando si se pudo finalizar la notificacion de desvio
     */
    @PostMapping("/finDesvio")
    public String finDesvio(@RequestBody String json) {
  
      Response<NotificacionDTO> response;
      String denomLinea = "", unidad = "", denomRecorrido = "";
  
      try {
        JSONObject obj = new JSONObject(json);
        denomLinea = obj.getString("linea");
        unidad = obj.getString("colectivo");
        denomRecorrido = obj.getString("recorrido");
      } catch (JSONException e) {
        e.printStackTrace();
      }
      boolean notificacionEstaActiva = false; // para saber que la notificacion termino
      ColectivoRecorrido cr = serviceMonitor.getColectivoRecorridoByLinDenomRecDenomColUnidad(denomLinea, denomRecorrido,
          unidad);
  
      Notificacion notificacion = serviceMonitor.finNotificacionDesvio(cr.getId(), notificacionEstaActiva);
      if (notificacion != null) {
        response = new Response<NotificacionDTO>(false, 200, "Desvio finalizado", NotificacionDTO.toDTO(notificacion));
      } else {
        response = new Response<NotificacionDTO>(true, 200, "No se pudo enviar la notificacion desvio", null);
      }
      return Mapper.getResponseAsJson(response);
    }
  
    /**
     * Metodo que utiliza app colectivo para enviar ubicacion en tiempo real del
     * colectivo en servicio.
     * Verifica si el colectivo esta dentro del trayecto habitual, si no lo esta
     * envia notificacion de desvio.
     * Si el colectivo estaba desviado y retoma el trayecto, finaliza la
     * notificacion de desvio
     * 
     * @param json datos enviados desde app colectivo, indica nombres de linea,
     *             recorrido, unidad y ubicacion en tiempo real del colectivo
     * @return indica si el colectivo esta en trayecto habitual o no
     */
    @PostMapping("/enviarUbicacion")
    public String enviarUbicacion(@RequestBody String json) {
  
      // pide inicializarla porque los response se cargan dentro de los if
      Response<NotificacionDTO> response = new Response<NotificacionDTO>(false, 200, json, null);
      String denomLinea = "", denomRecorrido = "", unidad = "", latitud = "", longitud = "";
  
      try {
        JSONObject obj = new JSONObject(json);
        denomLinea = obj.getString("linea");
        unidad = obj.getString("colectivo");
        denomRecorrido = obj.getString("recorrido");
        latitud = obj.getString("latitud");
        longitud = obj.getString("longitud");
      } catch (JSONException e) {
        e.printStackTrace();
      }
  
      ColectivoRecorrido cr = serviceMonitor.getColectivoRecorridoByLinDenomRecDenomColUnidad(denomLinea, denomRecorrido,
          unidad);
      Notificacion respNotificacion;
  
      serviceMonitor.createUbicacion(cr, latitud, longitud);
  
      // si devuelve true, es que esta en el recorrido
      Recorrido recorridoAct = serviceRecorrido.getRecorridoByLineaDenomYRecorridoDenom(denomLinea, denomRecorrido);
      Boolean enRecorrido = serviceRecorrido.verificarUnidadEnRecorrido(latitud, longitud, recorridoAct.getId());
      // si no esta en recorrido
      if (!enRecorrido) {
        respNotificacion = serviceMonitor.notificacionDesvio(cr, latitud, longitud, true);
        response = new Response<NotificacionDTO>(false, 200, "Notificacion enviada, el colectivo esta fuera del trayecto",
            NotificacionDTO.toDTO(respNotificacion));
      } else {
        // si esta en recorrido, verifica que no haya estado desviado, si lo esta setea
        // en false a activo
        Boolean colectivoEstaDesviado = serviceMonitor.desvioActivo(cr.getId());
  
        if (colectivoEstaDesviado) {
          respNotificacion = serviceMonitor.finNotificacionDesvio(cr.getId(), false);
          response = new Response<NotificacionDTO>(false, 200, "El colectivo retomo el trayecto habitual",
              NotificacionDTO.toDTO(respNotificacion));
        }
      }
  
      return Mapper.getResponseAsJson(response);
    }
  
    /**
     * Metodo
     * 
     * @param json
     * @return
     */
    @PostMapping("/enviarNotificacionColectivoDetenido")
    public String enviarNotificacion(@RequestBody String json) {
      Response<NotificacionDTO> response;
      String denomLinea = "", denomRecorrido = "", unidad = "", latitud = "", longitud = "", segundosDetenidoStr = "";
  
      try {
        JSONObject obj = new JSONObject(json);
        denomLinea = obj.getString("linea");
        unidad = obj.getString("colectivo");
        denomRecorrido = obj.getString("recorrido");
        latitud = obj.getString("latitud");
        longitud = obj.getString("longitud");
        // fechaNotificacion = obj.getString("fechaNotificacion");
        segundosDetenidoStr = obj.getString("segundosDetenidoStr");
      } catch (JSONException e) {
        e.printStackTrace();
      }
  
      Boolean notificacionEstaActiva = true; // para saber que la notificacion esta activa
      ColectivoRecorrido cr = serviceMonitor.getColectivoRecorridoByLinDenomRecDenomColUnidad(denomLinea, denomRecorrido,
          unidad);
      Notificacion respNotificacion = serviceMonitor.createNotificacion(cr, latitud, longitud, segundosDetenidoStr,
          notificacionEstaActiva);
  
      response = new Response<NotificacionDTO>(false, 200, "Notificacion enviada",
          NotificacionDTO.toDTO(respNotificacion));
  
      return Mapper.getResponseAsJson(response);
    }
  
    // trabajar en este metodo
  
    @PostMapping("/actualizarNotificacionColeDetenido")
    public String updateNotificacionColeDetenido(@RequestBody String json) {
  
      Response<NotificacionDTO> response;
  
      // Response<Notificacion> response = new Response<Notificacion>(false, 200,
      // json, null);
      String denomLinea = "", denomRecorrido = "", unidad = "", latitud = "", longitud = "", segundosDetenidoStr = "";
  
      // fechaNotificacion = "",
  
      try {
        JSONObject obj = new JSONObject(json);
        denomLinea = obj.getString("linea");
        unidad = obj.getString("colectivo");
        denomRecorrido = obj.getString("recorrido");
        latitud = obj.getString("latitud");
        longitud = obj.getString("longitud");
        // fechaNotificacion = obj.getString("fechaNotificacion");
        segundosDetenidoStr = obj.getString("segundosDetenidoStr");
      } catch (JSONException e) {
        e.printStackTrace();
      }
  
      Boolean notificacionEstaActiva = true; // para saber que la notificacion esta activa
  
      ColectivoRecorrido cr = serviceMonitor.getColectivoRecorridoByLinDenomRecDenomColUnidad(denomLinea, denomRecorrido,
          unidad);
  
      Notificacion respNotificacion = serviceMonitor.updateNotificacion(cr, latitud, longitud, segundosDetenidoStr,
          notificacionEstaActiva); // actualiza la descripcion con el nuevo tiempo
  
      response = new Response<NotificacionDTO>(false, 200, "Notificacion actualizada",
          NotificacionDTO.toDTO(respNotificacion));
  
      return Mapper.getResponseAsJson(response);
    }
  
    /**
     * Metodo utilizado por app colectivo para detener la notificacion de colectivo
     * detenido
     * 
     * @param json datos enviados por app colectivo, indica nombres de linea,
     *             recorrido, colectivo, ubicacion y cantidad de segundos detenidos
     * @return
     */
    @PostMapping("/finNotificacionColeDetenido")
    public String finNotificacionColeDetenido(@RequestBody String json) {
      Response<NotificacionDTO> response;
      String denomLinea = "", denomRecorrido = "", unidad = "", latitud = "", longitud = "", segundosDetenidoStr = "";
  
      try {
        JSONObject obj = new JSONObject(json);
        denomLinea = obj.getString("linea");
        unidad = obj.getString("colectivo");
        denomRecorrido = obj.getString("recorrido");
        latitud = obj.getString("latitud");
        longitud = obj.getString("longitud");
        // fechaNotificacion = obj.getString("fechaNotificacion");
        segundosDetenidoStr = obj.getString("segundosDetenidoStr");
      } catch (JSONException e) {
        e.printStackTrace();
      }
  
      Boolean notificacionEstaActiva = false; // para saber que la notificacion termino
      ColectivoRecorrido cr = serviceMonitor.getColectivoRecorridoByLinDenomRecDenomColUnidad(denomLinea, denomRecorrido,
          unidad);
      Notificacion respNotificacion = serviceMonitor.finNotificacionColeDetenido(cr, latitud, longitud,
          segundosDetenidoStr,
          notificacionEstaActiva); // actualiza la descripcion con el nuevo tiempo
      response = new Response<NotificacionDTO>(false, 200, "Notificacion finalizada",
          NotificacionDTO.toDTO(respNotificacion));
      return Mapper.getResponseAsJson(response);
    }
  
    // trabajar en este metodo para el codigo qr
    // para app pasajero
  
    /**
     * Meotodo utilizado por app pasajero para obtener tiempo llegada del proximo
     * colectivo
     * 
     * @param idLineaString      id linea a consultar
     * @param idRecorridoString  id recorrido a consultar
     * @param codigoParadaString id codigo de la parada a consultar
     * @return tiempo de llegada del proximo colectivo
     */
    @GetMapping("/obtenerTiempoLlegadaCole/{idLineaString}/{idRecorridoString}/{codigoParadaString}")
    public String obtenerTiempoLlegadaCole(@PathVariable String idLineaString, @PathVariable String idRecorridoString,
        @PathVariable String codigoParadaString) {
  
      Response<ColectivoRecorridoDTO> response;
      String separator = ":";
      int sepPos = idLineaString.indexOf(separator);
      long idLinea = Long.valueOf(idLineaString.substring(sepPos + separator.length()));
  
      int sepPos1 = idRecorridoString.indexOf(separator);
      long idRecorrido = Long.valueOf(idRecorridoString.substring(sepPos1 + separator.length()));
  
      int sepPos2 = codigoParadaString.indexOf(separator);
      long codigoParada = Long.valueOf(codigoParadaString.substring(sepPos2 + separator.length()));
  
      System.out.println("Los codigos obtenidos: idLinea: " + idLinea + " +++++++idRecorrido+++ " + idRecorrido
          + " +++++codigoParada++++++ " + codigoParada);
  
      // buscar colectivos en servicio de la linea
      List<ColectivoRecorrido> crList = serviceMonitor.findAllColectivosRecorridoActivos(idLinea, idRecorrido);
  
      // Buscar recorrido y todas sus paradas
      List<ParadaRecorrido> prlist = serviceRecorrido.getParadasRecorridoByLineaIdYRecorridoId(idLinea, idRecorrido);
  
      System.out.println("++++++++++++ lista paradas recorrido: " + prlist.size());
      for (ParadaRecorrido paradaRecorrido : prlist) {
        System.out
            .println(paradaRecorrido.getParada().toString() + " --- " + paradaRecorrido.getRecorrido().getDenominacion());
      }
  
      System.out.println("++++++++++++ lista colectivo recorrido activo" + crList.size());
      for (ColectivoRecorrido colectivoRecorrido : crList) {
        System.out.println(colectivoRecorrido.getColectivo().getUnidad() + " --- "
            + colectivoRecorrido.getRecorrido().getDenominacion());
      }
  
      // Buscar el orden de la parada del pasajero
      int ordenParadaPasajero = -1;
      for (ParadaRecorrido pr : prlist) {
        if (pr.getParada().getCodigo() == codigoParada) {
          ordenParadaPasajero = pr.getOrden();
          break;
        }
      }
  
      // Buscar el orden de la parada del colectivo
      int diferencia = 1000;
      ColectivoRecorrido cRecorrido = null;
      int ordenParadaColectivoSel = -1;
      List<ParadaRecorrido> paradasRec = new ArrayList<ParadaRecorrido>();
  
     
      
      try{
      for (ColectivoRecorrido cr : crList) {
        int ordenParadaColectivo = -1;
        for (ParadaRecorrido pr : prlist) {
          if (pr.getParada().getCodigo() == cr.getParadaActual().getCodigo()) {
            ordenParadaColectivo = pr.getOrden();
            int difparada = ordenParadaPasajero - ordenParadaColectivo;
            if (difparada > 0 && difparada < diferencia) {
              diferencia = difparada;
              cRecorrido = cr;
              ordenParadaColectivoSel = ordenParadaColectivo;
            }
            break;
          }
        }
      }
      }catch (NullPointerException e){
        // por si hay colectivos que no alcanzaron a llegar a una parada y estan desviados/detenidos
        System.out.println("+++++++++++++++++++++++++ ---------------------- +++++++++++++++++++++++");
        System.out.println("+++++++++++++++++++++++++ ---------------------- +++++++++++++++++++++++entro al catch de colectivos sin parada");
         response = new Response<ColectivoRecorridoDTO>(true, 400, "No hay colectivos cercanos", null);
        return Mapper.getResponseAsJson(response);
      }
  
      Double tiempoAcc = 0.0;
      boolean empezar = false;
      int ordenParadaActual = ordenParadaColectivoSel;
  
      for (ParadaRecorrido pr : prlist) {
        if (pr.getOrden() == ordenParadaColectivoSel) {
          if (empezar == false) {
            empezar = true;
            tiempoAcc = pr.getTiempo();
            ordenParadaActual++;
          }
        } else if (empezar && ordenParadaActual < ordenParadaPasajero) {
          tiempoAcc += pr.getTiempo();
          ordenParadaActual++;
        }
      }
  
      String tiempoTotal;
      int num, hor, min, seg;
      int segundos = tiempoAcc.intValue();
  
      hor = segundos / 3600;
      min = (segundos - (3600 * hor)) / 60;
      seg = segundos - ((hor * 3600) + (min * 60));
  
      if (tiempoAcc == 0) {
        response = new Response<ColectivoRecorridoDTO>(true, 400, "No hay colectivos cercanos", null);
        return Mapper.getResponseAsJson(response);
      }
  
      tiempoTotal = hor + " hs: " + min + " min: " + seg + " seg ";
      response = new Response<ColectivoRecorridoDTO>(false, 200, tiempoTotal, null);
      return Mapper.getResponseAsJson(response);
  
    } // fin metodo tiempo llegada
  
  
  } // fin clase
  