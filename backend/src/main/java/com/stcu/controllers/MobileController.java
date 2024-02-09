package com.stcu.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.DoubleBinaryOperator;
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

    List<Coordenada> listaCoordenadasASimular = new ArrayList<>();

    // trae recorrido activo
    Recorrido recorrido = serviceRecorrido.getRecorridoActivoByLineaDenomYRecorridoDenom(denomLinea, denomRecorrido);

    if (recorrido != null) {
      // invoca metodo que duplica waypoint del trayecto
      LineString recorridoCoordDensificado = densificarTrayecto(recorrido.getTrayectos());

      // invoca nuevamente al metodo para volver a densificar el trayecto
      LineString recorridoCoordDobleDensificado = densificarTrayecto(recorridoCoordDensificado);

      // por si quiero eliminar coordenadas repetidas. reemplazar por el codigo de
      // abajo
      boolean primeraPasada = true;
      Coordinate coordinateAux = null;

      System.out.println("trayecto doblemente densificado sin repetidos");
      for (Coordinate coordinate : recorridoCoordDobleDensificado.getCoordinates()) {
        if (primeraPasada) {
          coordinateAux = coordinate;
          primeraPasada = false;
        } else {
          if (coordinateAux.x != coordinate.x && coordinateAux.y != coordinate.y) {
            listaCoordenadasASimular.add(new Coordenada(coordinate.getX(), coordinate.getY()));
            System.out.println("coord x: " + coordinate.x + " - coord y: " + coordinate.y);
            coordinateAux = coordinate;
          }
        }
      }

      // densificado sin sacar duplicados
      // for (Coordinate coordinate : recorridoCoordDobleDensificado.getCoordinates())
      // {
      // listaCoordenadasASimular.add(new Coordenada(coordinate.getX(),
      // coordinate.getY()));
      // System.out.println("coordinate x y: " + coordinate.x + ", " + coordinate.y);
      // }

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

  private LineString densificarTrayecto(LineString trayectos) {

    // trae trayecto a recorrer
    LineString recorridoCoor = trayectos;

    // obtiene el factory
    GeometryFactory factory = recorridoCoor.getFactory();

    // empieza a armar el trayecto con mayor densidad
    CoordinateSequence sequenceOriginal = trayectos.getCoordinateSequence(); // coordenas original

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
    // System.out.println(
    // " +++++++++++++++++++ coordenadas a recorrer con mayor densidad:" +
    // recorridoCoordDensificado.toText());

    return recorridoCoordDensificado;
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
    String denomLinea = "", denomRecorrido = "", unidad = "", latitud = "", longitud = "",
        tiempoTotalSegIntDetenidoStr = "";

    try {
      JSONObject obj = new JSONObject(json);
      denomLinea = obj.getString("linea");
      unidad = obj.getString("colectivo");
      denomRecorrido = obj.getString("recorrido");
      latitud = obj.getString("latitud");
      longitud = obj.getString("longitud");
      // fechaNotificacion = obj.getString("fechaNotificacion");
      tiempoTotalSegIntDetenidoStr = obj.getString("tiempoTotalSegIntDetenidoStr");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    Boolean notificacionEstaActiva = true; // para saber que la notificacion esta activa
    ColectivoRecorrido cr = serviceMonitor.getColectivoRecorridoByLinDenomRecDenomColUnidad(denomLinea, denomRecorrido,
        unidad);
    Notificacion respNotificacion = serviceMonitor.createNotificacion(cr, latitud, longitud,
        tiempoTotalSegIntDetenidoStr,
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
    String denomLinea = "", denomRecorrido = "", unidad = "", latitud = "", longitud = "",
        tiempoTotalSegIntDetenidoStr = "";

    // fechaNotificacion = "",

    try {
      JSONObject obj = new JSONObject(json);
      denomLinea = obj.getString("linea");
      unidad = obj.getString("colectivo");
      denomRecorrido = obj.getString("recorrido");
      latitud = obj.getString("latitud");
      longitud = obj.getString("longitud");
      // fechaNotificacion = obj.getString("fechaNotificacion");
      tiempoTotalSegIntDetenidoStr = obj.getString("tiempoTotalSegIntDetenidoStr");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    Boolean notificacionEstaActiva = true; // para saber que la notificacion esta activa

    ColectivoRecorrido cr = serviceMonitor.getColectivoRecorridoByLinDenomRecDenomColUnidad(denomLinea, denomRecorrido,
        unidad);

    Notificacion respNotificacion = serviceMonitor.updateNotificacion(cr, latitud, longitud,
        tiempoTotalSegIntDetenidoStr,
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
   *             recorrido, colectivo, ubicacion y cantidad de tiempoTotalSegInt
   *             detenidos
   * @return
   */
  @PostMapping("/finNotificacionColeDetenido")
  public String finNotificacionColeDetenido(@RequestBody String json) {
    Response<NotificacionDTO> response;
    String denomLinea = "", denomRecorrido = "", unidad = "", latitud = "", longitud = "",
        tiempoTotalSegIntDetenidoStr = "";

    try {
      JSONObject obj = new JSONObject(json);
      denomLinea = obj.getString("linea");
      unidad = obj.getString("colectivo");
      denomRecorrido = obj.getString("recorrido");
      latitud = obj.getString("latitud");
      longitud = obj.getString("longitud");
      // fechaNotificacion = obj.getString("fechaNotificacion");
      tiempoTotalSegIntDetenidoStr = obj.getString("tiempoTotalSegIntDetenidoStr");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    Boolean notificacionEstaActiva = false; // para saber que la notificacion termino
    ColectivoRecorrido cr = serviceMonitor.getColectivoRecorridoByLinDenomRecDenomColUnidad(denomLinea, denomRecorrido,
        unidad);
    Notificacion respNotificacion = serviceMonitor.finNotificacionColeDetenido(cr, latitud, longitud,
        tiempoTotalSegIntDetenidoStr,
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

    Response<ArriboColectivoDTO> response;
    String separator = ":";
    int sepPos = idLineaString.indexOf(separator);
    long idLinea = Long.valueOf(idLineaString.substring(sepPos + separator.length()));

    int sepPos1 = idRecorridoString.indexOf(separator);
    long idRecorrido = Long.valueOf(idRecorridoString.substring(sepPos1 + separator.length()));

    int sepPos2 = codigoParadaString.indexOf(separator);
    long codigoParada = Long.valueOf(codigoParadaString.substring(sepPos2 + separator.length()));

    // buscar colectivos en servicio de la linea
    List<ColectivoRecorrido> crList = serviceMonitor.findAllColectivosRecorridoActivos(idLinea, idRecorrido);

    // Buscar recorrido y todas sus paradas
    List<ParadaRecorrido> prlist = serviceRecorrido.getParadasRecorridoByLineaIdYRecorridoId(idLinea, idRecorrido);

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
    int ordenParadaColectivoMasCercano = -1;
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
              ordenParadaColectivoMasCercano = ordenParadaColectivo;
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
      // por si hay colectivos que no alcanzaron a llegar a una parada o estan
      // desviados/detenidos

      // aca tambien ojo al responder. if data != null
      response = new Response<ArriboColectivoDTO>(true, 400, "No hay colectivos cercanos", null);
      return Mapper.getResponseAsJson(response);
    }

    // obtiene sumatoria distancias hasta que parada colectivo sea igual a parada
    // pasajero
    Double distanciaAcumulada = 0.0;
    boolean calculoDistanciaIniciada = false;
    int ordenParadaActual = ordenParadaColectivoMasCercano;

    for (ParadaRecorrido pr : prlist) {
      if (pr.getOrden() == ordenParadaColectivoMasCercano) {
        if (calculoDistanciaIniciada == false) {
          calculoDistanciaIniciada = true;
          ordenParadaActual++;
          distanciaAcumulada += pr.getDistancia(); //probar esto, sino sacarlo
        }
      } else if (calculoDistanciaIniciada && ordenParadaActual <= ordenParadaPasajero) {
        distanciaAcumulada += pr.getDistancia();
        ordenParadaActual++;
      }
    }

    if (colectivoEstaEnParadaPasajero) {
      response = new Response<ArriboColectivoDTO>(false, 400, "El colectivo llego a la parada", null);
      return Mapper.getResponseAsJson(response);
    }

    if (distanciaAcumulada == 0.0) {
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
    int VEL_PROMEDIO = 10000;
    Double velocidadReal = Double.valueOf(VEL_PROMEDIO);

    if (colRecProximo != null) {

      lat1Rad = Math.toRadians(colRecProximo.getParadaActual().getCoordenadas().getX());
      lon1Rad = Math.toRadians(colRecProximo.getParadaActual().getCoordenadas().getY());

      ultimaCoordColeRec = this.serviceMonitor.getLastUbicacion(colRecProximo.getId());
      lat2Rad = Math.toRadians(ultimaCoordColeRec.getCoordenada().getX());
      lon2Rad = Math.toRadians(ultimaCoordColeRec.getCoordenada().getY());

      Calendar calInicio = colRecProximo.getFechaParadaActual();
      Calendar calSalida = ultimaCoordColeRec.getFecha();

      double diferenciaTiempoMilis = calSalida.getTimeInMillis() - calInicio.getTimeInMillis();

      // para tiempo en segundos
      double diferenciaTiempoSecond = diferenciaTiempoMilis / 1000;

      // double minutes = diferenciaTiempoMilis / (60 * 1000) % 60;

      // double seconds = diferenciaTiempoMilis / 1000 % 60;

      // double secondsTotal = minutes * 60 + seconds;

      int RADIO_TIERRA = 6371;

      double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
      double y = (lat2Rad - lat1Rad);
      Double distanciaParcialTranscurrida = Math.sqrt(x * x + y * y) * RADIO_TIERRA;

      // Double distanciaParcialTranscurrida = RADIO_TIERRA * Math.acos(Math.sin(lat1Rad) *
      // Math.sin(lat2Rad))
      // + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.cos(lon1Rad - lon2Rad);

      Double distEnKms = distanciaParcialTranscurrida;
      // para distancia en metros
      distanciaParcialTranscurrida = distanciaParcialTranscurrida * 1000;

      // esto es para saber la velocidad
      // System.out.println("distancia transcurrida ms: " + distanciaParcialTranscurrida + "
      // / seconds pasado a hs: " + (secondsTotal / 60 / 60));

      System.out.println(
          "()()()()()()() Distancia transcurrida en mts desde ultima parada visitada: " + distanciaParcialTranscurrida);
      // System.out.println(" +++++++++++++++++++++++ ");
      System.out.println("%%%%%%%%%%%%%%% segundos trascurridos desde ultima parada visitada" + diferenciaTiempoSecond
          + " - o en hs: " + (diferenciaTiempoSecond / 60 / 60));

      if (diferenciaTiempoSecond > 1) {
        // velocidadReal = distanciaParcialTranscurrida.intValue() /
        // tiempoTotalSegIntTranscurridos;
        // velocidadReal = distEnKms / hsTranscurridas;

        // dist en kms / hs
        velocidadReal = distanciaParcialTranscurrida / (diferenciaTiempoSecond / 60 / 60);

        // aca hacer el calculo de tiempo. con distancia total
      }

      System.out.println("&&&&&&&&&&&&&&& Velocidad a la que circulo el cole desde parada visitada: " + velocidadReal);

      tiempoSobrante = (distanciaParcialTranscurrida / velocidadReal) * 60 * 60;

      System.out.println("·········································" );
      System.out.println("·······················tiempo sobrante para restar" + tiempoSobrante);

    }

    // tiempoTotalSeg = distanciaAcumulada - tiempoSobranteEnriquecido;

    // primero esto para obtener un numero entre paradas
    int velocidadPromediada = (colRecProximo.getColectivo().getVelocidadPromedio() + velocidadReal.intValue()) / 2;
    serviceMonitor.updateVelPromColectivoRecorrido(colRecProximo, velocidadPromediada);

    Double tiempoTotalSeg = (distanciaAcumulada / velocidadPromediada) * 3600;

    tiempoTotalSeg = tiempoTotalSeg - tiempoSobrante;
    /*
     * System.out.println("-----------------------------------------------");
     * System.out.println("distancia acumulada: " + distanciaAcumulada +
     * " / velocidad elegida: " + velocidadReal);
     * System.out.println("-----------------------------------------------");
     * System.out.println("tiempo Total seg: " + tiempoTotalSeg);
     */

    // hacer esto despues para una mayor precision
    // Double tiempoTotalSeg = distanciaAcumulada - tiempoSobrante;

    // todavia no uso tiempo sobrante
    // System.out.println("tiempo sobrante: " + tiempoSobrante);

    int tiempoTotalSegInt = tiempoTotalSeg.intValue();

    // para darle formato hs:min:seg
    String tiempoTotal;
    int hor, min, seg;

    hor = tiempoTotalSegInt / 3600;
    min = (tiempoTotalSegInt - (3600 * hor)) / 60;
    seg = tiempoTotalSegInt - ((hor * 3600) + (min * 60));

    tiempoTotal = hor + " hs: " + min + " min: " + seg + " seg ";

    ArriboColectivoDTO acDTO = new ArriboColectivoDTO(colRecProximo.getFechaParadaActual(),
        tiempoTotal, colRecProximo.getParadaActual(), paradaPasajero.getParada());

    if ((hor == 0 && min == 0 && seg < 10)) {
      System.out.println("Colectivo aproximandose a la parada"); // y return
      response = new Response<ArriboColectivoDTO>(false, 200, "Colectivo aproximandose a la parada",
          ArriboColectivoDTO.toColectivoRecorridoDTO(acDTO));
      return Mapper.getResponseAsJson(response);
    }

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
