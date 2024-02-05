package com.stcu.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateList;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.*;

import org.postgis.MultiPoint;
import org.postgis.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.aspectj.weaver.ast.Test;
import org.json.JSONException;
import org.json.JSONObject;

import com.stcu.dto.response.ArriboColectivoDTO;
import com.stcu.dto.response.ColectivoRecorridoDTO;
import com.stcu.dto.response.CoordenadaDTO;
import com.stcu.dto.response.NotificacionDTO;
import com.stcu.dto.response.ParadaRecorridoDTO;
import com.stcu.dto.response.RecorridoDTO;
import com.stcu.model.Colectivo;
import com.stcu.model.ColectivoRecorrido;
import com.stcu.model.Coordenada;
import com.stcu.model.Linea;
import com.stcu.model.Notificacion;
import com.stcu.model.Parada;
import com.stcu.model.ParadaRecorrido;
import com.stcu.model.Recorrido;
import com.stcu.model.Ubicacion;
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

    if (recorridosActivos.isEmpty()) {
      Response<List<RecorridoDTO>> response = new Response<List<RecorridoDTO>>(true, 400,
          "No hay recorridos activos para la linea " + denomLinea, null);
      return Mapper.getResponseAsJson(response);

    }
    Response<List<RecorridoDTO>> response = new Response<List<RecorridoDTO>>(false, 200,
        "Recorridos activos " + denomLinea, RecorridoDTO.toListRecorridosDTO(recorridosActivos));
    return Mapper.getResponseAsJson(response);

  }

  /**
   * Busca el trayecto a recorrer y sus paradas correspondientes. obsoleto.
   * devolvia solo paradasRecorrido y no waypoints
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

  /**
   * Busca los waypoints del trayecto a recorrer por el simulador de recorrido
   * 
   * @param denomLinea     denominacion linea
   * @param denomRecorrido denominacion recorrido
   * @return lista de paradas a recorrer
   */
  @GetMapping("/trayectos/simulacion/{denomLinea}/{denomRecorrido}")
  public String findTrayectosRecorridoSimulacion(@PathVariable String denomLinea, @PathVariable String denomRecorrido)
      throws IOException {

    // esto no anda. cambiar si lo necesito
    // listaCoordenadasASimular =
    // listaCoordenadasASimular.stream().distinct().collect(Collectors.toList());
    // for (Coordenada coordenadaSinRepetidos : listaCoordenadasASimular) {
    // System.out.println("coordinate x: " + coordenadaSinRepetidos.getLat() + " -
    // coordinate y: " + coordenadaSinRepetidos.getLng());
    // }

    /*
     * por si lo necesito, para eliminar repetidos
     * List auxArray = arrayList.clone();
     * // Esto recorre todos los objetos del arrayList pero comparando y eliminando
     * en el auxiliar
     * for (Obj obj : arrayList) {
     * while (auxArray.indexOf(obj) != auxArray.lastIndexOf(obj)) {
     * auxArray.remove(lastIndexOf(obj));
     * }
     * }
     * // Finalmente
     * arrayList = auxArray();
     */

    List<Coordenada> listaCoordenadasASimular = new ArrayList<>();

    // trae recorrido activo
    Recorrido recorrido = serviceRecorrido.getRecorridoActivoByLineaDenomYRecorridoDenom(denomLinea, denomRecorrido);

    if (recorrido != null) {

      // trae trayecto a recorrer
      LineString recorridoCoor = recorrido.getTrayectos();
      System.out.println(" +++++++++++++++++++ coordenadas a recorrer original" + recorridoCoor.toText());

      // obtiene el factory
      GeometryFactory factory = recorridoCoor.getFactory();

      // empieza a armar el trayecto con mayor densidad
      CoordinateSequence sequenceOriginal = recorrido.getTrayectos().getCoordinateSequence(); // coordenas original

      CoordinateList listFinal = new CoordinateList(); // lista que va a tener mas coordenadas que el original

      for (int i = 0; i < recorridoCoor.getNumPoints() - 1; i++) {
        // crea lineString entre punto actual y punto siguiente
        LineString lineStringIntermedio = factory.createLineString(
            new Coordinate[] { recorridoCoor.getCoordinateN(i), recorridoCoor.getCoordinateN(i + 1) });

        // pasa el linestring intermedio al que crea punto intermedio
        String pointStr = serviceRecorrido.crearPuntoIntermedio(lineStringIntermedio);

        // deserializador
        pointStr = pointStr.replaceAll("[()]", "");
        pointStr = pointStr.replaceAll("POINT", "");
        String[] latLngPoint = pointStr.split(" ");

        // agrega la coordenada original
        listFinal.add(sequenceOriginal.getCoordinate(i));
        // luego agrega la coordenada intermedia generada
        if (i < recorridoCoor.getNumPoints() - 2) // para que no entre a la ultima
          listFinal.add(new Coordinate(Double.parseDouble(latLngPoint[0]), Double.parseDouble(latLngPoint[1])));

      }

      // crea lista de coordenadas para return

      LineString recorridoCoordDensificado = factory.createLineString(listFinal.toCoordinateArray());
      System.out.println(
          " +++++++++++++++++++ coordenadas a recorrer con mayor densidad:" + recorridoCoordDensificado.toText());

      for (Coordinate coordinate : recorridoCoordDensificado.getCoordinates()) {
        listaCoordenadasASimular.add(new Coordenada(coordinate.getX(), coordinate.getY()));
        System.out.println("coordinate x y: " + coordinate.x + ", " + coordinate.y);
      }
    }

    Response<List<CoordenadaDTO>> response;

    if (listaCoordenadasASimular.size() > 0) {
      response = new Response<List<CoordenadaDTO>>(false, 200, "Trayecto a simular para recorrido " + recorrido.getId(),
          CoordenadaDTO.toListCoordenadaDTO(listaCoordenadasASimular));
      return Mapper.getResponseAsJson(response);
    }

    response = new Response<List<CoordenadaDTO>>(true, 400,
        "No se pudo recuperar recorrido " + recorrido.getId() + " a simular", null);
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
    // System.out.println("Colectivo: " + unidad + " Linea: " + denomLinea + "
    // CodParada: " + codigoParada);

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

    // System.out.println("parametros ENVIO DESVIO ++++++++++++++++ " + denomLinea +
    // " " + unidad + " " + denomRecorrido
    // + " " + latitud + " " + longitud);
    ColectivoRecorrido cr = serviceMonitor.getColectivoRecorridoByLinDenomRecDenomColUnidad(denomLinea, denomRecorrido,
        unidad);
    // si devuelve true, esta en el recorrido
    Recorrido recorridoAct = serviceRecorrido.getRecorridoByLineaDenomYRecorridoDenom(denomLinea, denomRecorrido);
    Boolean enRecorrido = serviceRecorrido.verificarUnidadEnRecorrido(latitud, longitud, recorridoAct.getId());
    // System.out.println(" ++++++++++++++++ el resultado de verificar unidad en
    // recorrido +++++++++ " + enRecorrido);
    // si no esta en recorrido
    Notificacion respNotificacion = new Notificacion();
    if (!enRecorrido) {
      try { // ver que parametro usar para atrapar el error
        respNotificacion = serviceMonitor.notificacionDesvio(cr, latitud, longitud, true);
        // System.out.println(" ++++++++++++++++ notificacion enviada, el colectivo esta
        // fuera del trayecto");
        response = new Response<NotificacionDTO>(false, 200,
            "Notificacion enviada, el colectivo esta fuera del trayecto!",
            NotificacionDTO.toDTO(respNotificacion));
      } catch (NullPointerException e) { // si da error por colectivo nulo
        response = new Response<NotificacionDTO>(true, 400,
            "Error al enviar notificacion!",
            null);
      }
    } else {
      // si esta en recorrido, verifica que no haya estado desviado, si lo esta setea
      // colectivo en false
      Boolean colectivoEstaDesviado = serviceMonitor.desvioActivo(cr.getId());
      // System.out.println(" ++++++++++++++++ Habia desvio activo? " +
      // colectivoEstaDesviado);
      if (colectivoEstaDesviado) {
        respNotificacion = serviceMonitor.finNotificacionDesvio(cr.getId(), false);
        // System.out.println(" ++++++++++++++++ notificacion desactivada, el colectivo
        // retomo el trayecto habitual");
        response = new Response<NotificacionDTO>(false, 200,
            "El colectivo estaba desviado, retomo el trayecto habitual",
            NotificacionDTO.toDTO(respNotificacion));
      } else {
        // System.out.println(" ++++++++++++++++ el colectivo esta dentro del trayecto
        // habitual");
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

    // set transito false, no lo da de baja
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
    // ColectivoRecorrido cr = serviceMonitor.bajaColectivoRecorrido(denomLinea,
    // unidad, denomRecorrido, Calendar.getInstance(), enTransito);
    ColectivoRecorrido cr = serviceMonitor.detenerColectivoRecorridoByDenom(denomLinea, unidad, denomRecorrido,
        enTransito);

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

    // System.out.println("+++++++++++++++++ entra en obtener tiempo llegada cole");

    Response<ArriboColectivoDTO> response;
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

    // System.out.println("++++++++++++ lista paradas recorrido: " + prlist.size());
    for (ParadaRecorrido paradaRecorrido : prlist) {
      System.out
          .println(paradaRecorrido.getParada().toString() + " --- " + paradaRecorrido.getRecorrido().getDenominacion());
    }

    // System.out.println("++++++++++++ lista colectivo recorrido activo" +
    // crList.size());
    for (ColectivoRecorrido colectivoRecorrido : crList) {
      System.out.println(colectivoRecorrido.getColectivo().getUnidad() + " --- "
          + colectivoRecorrido.getRecorrido().getDenominacion());
    }

    // Buscar el orden de la parada del pasajero
    int ordenParadaPasajero = -1;
    ParadaRecorrido paradaPasajero = null;
    for (ParadaRecorrido pr : prlist) {
      if (pr.getParada().getCodigo() == codigoParada) {
        ordenParadaPasajero = pr.getOrden();
        paradaPasajero = pr;
        break;
      }
    }

    // Buscar el orden de la parada del colectivo
    int diferencia = 1000;
    ColectivoRecorrido colRecProximo = null;
    int ordenParadaColectivoSel = -1;
    boolean colectivoEstaEnParadaPasajero = false;

    try {
      for (ColectivoRecorrido cr : crList) {
        int ordenParadaColectivo = -1;
        for (ParadaRecorrido pr : prlist) {
          if (pr.getParada().getCodigo() == cr.getParadaActual().getCodigo()) {
            ordenParadaColectivo = pr.getOrden();
            int difparada = ordenParadaPasajero - ordenParadaColectivo;
            if (difparada > 0 && difparada < diferencia) {
              diferencia = difparada;
              colRecProximo = cr;
              ordenParadaColectivoSel = ordenParadaColectivo;
            }
            // si el colectivo esta en la parada del pasajero
            if (ordenParadaPasajero == ordenParadaColectivo) {
              colectivoEstaEnParadaPasajero = true;
            }
            break;
          }
        }
      }
    } catch (NullPointerException e) {
      // por si hay colectivos que no alcanzaron a llegar a una parada y estan
      // desviados/detenidos


      // aca tambien ojo al responder. if data != null
      response = new Response<ArriboColectivoDTO>(true, 400, "No hay colectivos cercanos", null);
      return Mapper.getResponseAsJson(response);
    }

    // obtiene sumatoria de tiempos hasta que la parada del colectivo sea igual a la
    // parada del pasajero
    Double tiempoAcumulado = 0.0;
    boolean calculoTiempoIniciado = false;
    int ordenParadaActual = ordenParadaColectivoSel;

    for (ParadaRecorrido pr : prlist) {
      if (pr.getOrden() == ordenParadaColectivoSel) {
        if (calculoTiempoIniciado == false) {
          calculoTiempoIniciado = true;
          // tiempoAcumulado = pr.getTiempo();
          ordenParadaActual++;
        }
      } else if (calculoTiempoIniciado && ordenParadaActual <= ordenParadaPasajero) {
        tiempoAcumulado += pr.getTiempo();
        ordenParadaActual++;
      }
    }

    // System.out.println(" colectivoEstaEnParadaPasajero: " +
    // colectivoEstaEnParadaPasajero);


    // ojo estos dos casos, no tendria que ir al mapa! ver como identificarlo
    // if data != null en android
    if (colectivoEstaEnParadaPasajero) {
      System.out.println("El colectivo llego a la parada"); // y return
      response = new Response<ArriboColectivoDTO>(false, 400, "El colectivo llego a la parada", null);
      return Mapper.getResponseAsJson(response);
    }

    if (tiempoAcumulado == 0.0) {
      response = new Response<ArriboColectivoDTO>(true, 400, "No hay colectivos cercanos", null);
      return Mapper.getResponseAsJson(response);
    }

    // al tiempo acumulado, restarle tiempo el tiempo de coordenada actual - tiempo
    // ultima parada visitada

    
    double lat1Rad = 0;
    double lon1Rad = 0;

    double lat2Rad = 0;
    double lon2Rad = 0;

    Double tiempoSobrante = 0.0;

    Ubicacion ultimaCoordColeRec;
    if (colRecProximo != null) {

      lat1Rad = Math.toRadians(colRecProximo.getParadaActual().getCoordenadas().getX());
      lon1Rad = Math.toRadians(colRecProximo.getParadaActual().getCoordenadas().getY());

      ultimaCoordColeRec = this.serviceMonitor.getLastUbicacion(colRecProximo.getId());
      lat2Rad = Math.toRadians(ultimaCoordColeRec.getCoordenada().getX());
      lon2Rad = Math.toRadians(ultimaCoordColeRec.getCoordenada().getY());

      /*
       * Calendar calInicio = colRecProximo.getFechaParadaActual();
       * Calendar calSalida = ultimaCoordColeRec.getFecha();
       * 
       * int difHoras = calSalida.get(Calendar.HOUR) - calInicio.get(Calendar.HOUR);
       * int difMinutos = calSalida.get(Calendar.MINUTE) -
       * calInicio.get(Calendar.MINUTE);
       * int difSegundos = calSalida.get(Calendar.SECOND) -
       * calInicio.get(Calendar.SECOND);
       * Calendar calDif = Calendar.getInstance();// variable para diferencia de
       * tiempo
       * 
       * calDif.set(Calendar.HOUR, difHoras);
       * calDif.set(Calendar.MINUTE, difMinutos);
       * calDif.set(Calendar.SECOND, difSegundos);
       * int segundosTranscurridos = calDif.get(Calendar.HOUR) * 60 * 60 +
       * calDif.get(Calendar.MINUTE) * 60 + calDif.get(Calendar.SECOND);
       * 
       * double hsTranscurridas = segundosTranscurridos/60/60;
       * 
       * System.out.println("----- ");
       * System.out.println("horas diferencia" + calDif.get(Calendar.HOUR));
       * System.out.println("minutos diferencia" + calDif.get(Calendar.MINUTE));
       * System.out.println("segundos diferencia" + calDif.get(Calendar.SECOND));
       * System.out.println("----- ");
       */
      int EARTH_RADIUS = 6371;
      int VEL_PROMEDIO = 25000;

      double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
      double y = (lat2Rad - lat1Rad);
      Double distanciaTranscurrida = Math.sqrt(x * x + y * y) * EARTH_RADIUS;

      // Double distEnKms = distanciaTranscurrida;
      // para distancia en metros
      distanciaTranscurrida = distanciaTranscurrida * 1000;

      /*
       * double velocidadEnriquecida;
       * if (segundosTranscurridos != 0){
       * //velocidadEnriquecida = distanciaTranscurrida.intValue() /
       * segundosTranscurridos;
       * velocidadEnriquecida = distEnKms / hsTranscurridas;
       * System.out.
       * println("()()()()()()()()()()()()()()() Distancia transcurrida desde ultima parada visitada: "
       * + distEnKms);
       * System.out.println(" +++++++++++++++++++++++ ");
       * System.out.
       * println(" %%%%%%%%%%%%%%%%%%%%%% Total segundos trascurridos desde ultima parada visitada"
       * + hsTranscurridas);
       * System.out.
       * println(" &&&&&&&&&&&&&&&&&&&&&& Velocidad a la que circulo el cole desde parada visitada: "
       * + velocidadEnriquecida);
       * }
       */

      // tiempoSobranteEnriquecido = (distanciaTranscurrida / velocidadEnriquecida)
      // *60 *60
      tiempoSobrante = (distanciaTranscurrida / VEL_PROMEDIO) * 60 * 60;

    }

    // segundosDouble = tiempoAcumulado - tiempoSobranteEnriquecido;
    Double segundosDouble = tiempoAcumulado - tiempoSobrante;


    System.out.println("-----------------------------------------------");
    System.out.println("tiempo acumulado: "+ tiempoAcumulado);
    System.out.println("tiempo sobrante: "+ tiempoSobrante);

    int segundos = segundosDouble.intValue();

    // para darle formato hs:min:seg
    String tiempoTotal;
    int hor, min, seg;

    hor = segundos / 3600;
    min = (segundos - (3600 * hor)) / 60;
    seg = segundos - ((hor * 3600) + (min * 60));

    


    tiempoTotal = hor + " hs: " + min + " min: " + seg + " seg ";

    // para responder dto
    // tambien agregarle el recorrido faltante para ponerlo en el mapa
    ArriboColectivoDTO acDTO = new ArriboColectivoDTO(colRecProximo.getFechaParadaActual(), 
    tiempoTotal, colRecProximo.getParadaActual(), paradaPasajero.getParada());


    // hacer lo del recorrido sobrante
    List<Coordinate> listaCoorRestantes = new ArrayList<Coordinate>();
    Boolean coordenadasSinTransitar = false;

    Recorrido recorrido = serviceRecorrido.getRecorrido(idRecorrido);

    //if (recorrido != null) {
      // trae trayecto a recorrer
      LineString recorridoCoor = recorrido.getTrayectos();
    //}
      
    
    org.locationtech.jts.geom.Point p = paradaPasajero.getParada().getCoordenadas();
    BigDecimal bigDecimalLatParadaPasajero = new BigDecimal(p.getX()).setScale(4,RoundingMode.DOWN);
    BigDecimal bigDecimalLngParadaPasajero = new BigDecimal(p.getY()).setScale(4,RoundingMode.DOWN);
    //tambien voy a necesitar las coordenadas del pasajero. tengo que parar de guardar ahi. set bandera false.

    // si no es preciso, tratar de guardar el recorrido densificado creado en trayecto a simular
    double latParadaColectivo = colRecProximo.getParadaActual().getCoordenadas().getX();
    double lngParadaColectivo = colRecProximo.getParadaActual().getCoordenadas().getY();
    
    BigDecimal bigDecimalLat = new BigDecimal(latParadaColectivo).setScale(4,RoundingMode.DOWN);
    BigDecimal bigDecimalLng = new BigDecimal(lngParadaColectivo).setScale(4,RoundingMode.DOWN);

    for (int i = 0; i < recorridoCoor.getNumPoints() - 1; i++) {

      BigDecimal bigDecimalLatActual = new BigDecimal(recorridoCoor.getCoordinateN(i).getX()).setScale(4,RoundingMode.DOWN);
      BigDecimal bigDecimalLngActual = new BigDecimal(recorridoCoor.getCoordinateN(i).getY()).setScale(4,RoundingMode.DOWN);

      if(coordenadasSinTransitar){
        listaCoorRestantes.add(recorridoCoor.getCoordinateN(i));
        // if(coordenada actual == coordenada pasajero) // ya no tiene que guardar coordenadas
        if(bigDecimalLatActual.doubleValue() == bigDecimalLatParadaPasajero.doubleValue() && bigDecimalLngActual.doubleValue() == bigDecimalLngParadaPasajero.doubleValue()){
          coordenadasSinTransitar = false;
        }        
      }else{
        System.out.println("coordenadas a comparar: "+ bigDecimalLat.doubleValue() + " - " + recorridoCoor.getCoordinateN(i).getX());
        // if cordenada actual == coordenada parada actual colectivo
        if( bigDecimalLat.doubleValue() ==  bigDecimalLatActual.doubleValue() && bigDecimalLng.doubleValue() ==  bigDecimalLngActual.doubleValue()){
          coordenadasSinTransitar = true;
          listaCoorRestantes.add(recorridoCoor.getCoordinateN(i));
        }
      }      
    }


    System.out.println("**********************************");
    System.out.println("coordenadas recuperada por transitar");
    for (Coordinate coordinate : listaCoorRestantes) {
      System.out.println(coordinate.x + " - " + coordinate.y);
    }


    if ((hor == 0 && min == 0 && seg < 10)) {
      System.out.println("Colectivo aproximandose a la parada"); // y return
      response = new Response<ArriboColectivoDTO>(false, 200, "Colectivo aproximandose a la parada",
      ArriboColectivoDTO.toColectivoRecorridoDTO(acDTO));
      return Mapper.getResponseAsJson(response);
    }



    
    // en el response dto necesito
    // coord parada pasajero
    // tiempo arribo colectivo (el que ya tenia) -> tiempoTotal
    // utlima parada que visito el colRecProximo 
    // fecha en que visito la ultima prada actual

    response = new Response<ArriboColectivoDTO>(false, 200, tiempoTotal,
    ArriboColectivoDTO.toColectivoRecorridoDTO(acDTO));
    return Mapper.getResponseAsJson(response);

  } // fin metodo tiempo llegada

  // esta clase no la necesitaria si hago el nuevo dto
  /*
   * @GetMapping(
   * "/obtenerUbicacionParadaRecorrido/{idLineaString}/{idRecorridoString}/{codigoParadaString}")
   * public String obtenerUbicacionParadaRecorrido(@PathVariable String
   * idLineaString,
   * 
   * @PathVariable String idRecorridoString,
   * 
   * @PathVariable String codigoParadaString) {
   * 
   * Response<ParadaRecorridoDTO> response;
   * 
   * String separator = ":";
   * int sepPos = idLineaString.indexOf(separator);
   * long idLinea = Long.valueOf(idLineaString.substring(sepPos +
   * separator.length()));
   * 
   * int sepPos1 = idRecorridoString.indexOf(separator);
   * long idRecorrido = Long.valueOf(idRecorridoString.substring(sepPos1 +
   * separator.length()));
   * 
   * int sepPos2 = codigoParadaString.indexOf(separator);
   * long codigoParada = Long.valueOf(codigoParadaString.substring(sepPos2 +
   * separator.length()));
   * 
   * ParadaRecorrido pr = serviceRecorrido.getParadaRecorrido(idLinea,
   * idRecorrido, codigoParada);
   * 
   * // envia las coordenadas de la paradaRecorrido correspondiente a la ubicacion
   * // del usuario
   * response = new Response<ParadaRecorridoDTO>(false, 200,
   * pr.getParada().getCoordenadas().getX() + "," +
   * pr.getParada().getCoordenadas().getY(),
   * null);
   * return Mapper.getResponseAsJson(response);
   * 
   * }
   */

} // fin clase
