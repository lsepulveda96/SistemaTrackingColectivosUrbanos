package com.stcu.services;

import java.util.Calendar;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stcu.model.ColectivoRecorrido;
import com.stcu.model.Notificacion;
import com.stcu.model.Ubicacion;
import com.stcu.repository.ColectivoRecorridoRepository;
import com.stcu.repository.NotificacionRepository;
import com.stcu.repository.ParadaRepository;
import com.stcu.repository.RecorridoRepository;
import com.stcu.repository.UbicacionRepository;
import org.locationtech.jts.geom.Point;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class MonitorServiceImp implements MonitorService {

    @Autowired
    private ColectivoRecorridoRepository repoColRec;
    @Autowired
    private UbicacionRepository repoUbicacion;
        @Autowired
    private ParadaRepository repoParada;
    @Autowired
    private NotificacionRepository repoNotificacion;

    @Autowired
    private RecorridoRepository repoRecorrido;

    

    @Override
    public List<ColectivoRecorrido> getColectivosTransito() {
        //return this.repoColRec.findByTransitoTrue();
        return this.repoColRec.findByTransitoTrueOrderByIdDesc();
    }

    @Override
    public ColectivoRecorrido saveColectivoRecorrido(ColectivoRecorrido cr) {
        cr.getColectivo().setEnCirculacion(true);
        cr.setTransito(true);
        cr.setDesde(Calendar.getInstance());
        cr.setHasta(null);
        return this.repoColRec.save( cr );
    }

    @Override
    public Ubicacion saveUbicacion( Ubicacion ubicacion ) {
        System.out.println("******************SAVE UBICACION: " + ubicacion.toString() );
        return this.repoUbicacion.save(ubicacion);
    }

    @Override
    public Ubicacion getLastUbicacion(long cr) {
        System.out.println("****************** GET LAST UBICACION... ");
        return this.repoUbicacion.findLastByColectivoRecorrido( cr );
    }

    @Override
    public List<Ubicacion> findUbicaciones(long cr) {
        return this.repoUbicacion.findByColectivoRecorrido( cr );
    }

    @Override
    public ColectivoRecorrido getColectivoRecorrido(long idcr) {
        return this.repoColRec.findById( idcr );
    }

    
    //para app colectivo
    @Override
    public ColectivoRecorrido updateParadaActual(String denomLinea, String denomRecorrido, String unidad, Long codigoParada ) {

        try {
            ColectivoRecorrido cr = repoColRec.findColectivoRecorrido(denomLinea, denomRecorrido, unidad);
            //Colectivo colectivo = this.rep.findById(id);
            if (cr != null) {
                cr.setParadaActual(repoParada.findByCodigo(codigoParada));
                cr.setFechaParadaActual(Calendar.getInstance());
                return this.repoColRec.save(cr);
            }
            return null;
        } catch (Exception ex) {
            System.out.println("++++++ ERROR actualizando parada actual " + ex);
            return null;
        }
    }


    //para app colectivo
    @Override
    public ColectivoRecorrido getColectivoRecorridoByLinDenomRecDenomColUnidad(String denomLinea,String denomRecorrido,String unidad){
        return this.repoColRec.findColectivoRecorrido(denomLinea,denomRecorrido,unidad);
    }


//para app colectivo
    @Override
    public ColectivoRecorrido getCrByLinRecColDenomFueraDeCirculacion(String denomLinea,String denomRecorrido,String unidad){
        return this.repoColRec.findColectivoRecorridoFueraDeCiruclacion(denomLinea,denomRecorrido,unidad);
    }
    

    //para app colectivo
    //crea una notificacion de tipo "Desviado", y activa = true, con fecha actual
    @Override
    public Notificacion notificacionDesvio(ColectivoRecorrido cr, String latitud, String longitud, boolean notificacionEstaActiva) {
        //this.repoNotificacion.notificacionDesvio(cr,latitud,longitud,currentDate,strDate,b); //crear un nuevo repositorio de notificacion?.
    
        System.out.println("+++++++++++++++++++++++++++++");
        System.out.println("SAVE NOTIFICACION");
        System.out.println("+++++++++++++++++++++++++++++");
        //para guardar las coordenadas x y 
        GeometryFactory geometryFactory = new GeometryFactory();
        Point coordenadasNotificacion = geometryFactory.createPoint(new Coordinate(Double.parseDouble(latitud), Double.parseDouble(longitud)));
        //parada.setCoordenadas( point );
        Notificacion notificacion = new Notificacion();
        notificacion.setFecha(Calendar.getInstance());
        notificacion.setColectivoRecorrido(cr);
        String descripcion = "Unidad Desviada";
        notificacion.setDescripcion(descripcion);
        notificacion.setActiva(notificacionEstaActiva);
        notificacion.setTipo("DESVIADO");
        notificacion.setCoordenadas(coordenadasNotificacion);

        this.repoNotificacion.save(notificacion);
        //ante creaba la coordenada y desps le hacia el update metiendo la coordenada que quiere
        return notificacion;
    }



    //para app colectivo
    // para saber si hay una notificacion de desvio activa, si la hay y no esta desviado, la cierra
    @Override
    public Boolean desvioActivo(long idcr){       

        //try {
            //si encuentra la notificacion retorna true, sino false
            Notificacion notificacion = this.repoNotificacion.findDesvioActivo(idcr);

            if(notificacion != null){
            //System.out.println("+++++++++++++++++++++ La notificacion que estaba desviada ");
            //System.out.println("notificacion: " + notificacion.getDescripcion() +" "+ notificacion.getId() +" " + notificacion.getTipo());
            //System.out.println("+++++++++++++++++++++ hay desvio activo ");
            return true;   
            }else{
               // System.out.println("+++++++++++++++++++++ No hay desvio activo ");
                return false;
            }
           
       // }catch(NoResultException ex ){
       // System.out.println("+++++++++++++++++++++ No hay desvio activo ");
       //     return false;
       // }
        
    }

    //para app colectivo
    @Override
    @Transactional
    public Notificacion finNotificacionDesvio(long idcr, boolean notificacionEstaActiva) {
        Notificacion notificacion = this.repoNotificacion.findDesvioActivo(idcr);
        if (notificacion != null) {
            notificacion.setActiva(notificacionEstaActiva); 
            notificacion.setFecha(Calendar.getInstance());
            this.repoNotificacion.save(notificacion);
        }
            return notificacion;
        }




    //para app colectivo
    @Override    
    public ColectivoRecorrido bajaColectivoRecorrido(String denomLinea, String unidad, String denomRecorrido, Calendar fechaFin, boolean enTransito) {
    /*
    * la baja hace:
    get colectivoRecorrido by recorrido y colectivo
    colectivoRecorrido.setFechBaja(fecha actual)
    colectivoRecorrido.getColectivo().setEnCirculacion(false);
    */
         ColectivoRecorrido cr = this.repoColRec.findColectivoRecorrido(denomLinea, denomRecorrido, unidad);

         if (cr != null) {
         cr.setHasta(fechaFin);
         cr.getColectivo().setEnCirculacion(enTransito);
         cr.setTransito(enTransito);
         this.repoColRec.save(cr);
         System.out.println(" +++++++++++++++++++++-----------------++++++ colectivo dado de baja con exito: " + " en transito: " + enTransito + ", recorrido denom " + cr.getRecorrido().getDenominacion() + ", unidad denom" + cr.getColectivo().getUnidad());
         }else{
            System.out.println("++++++++++++++++++------------------++++++++++++++++++No se encontro el recorrido a dar de baja!!!!");
         }

        return cr;
    }

    public Ubicacion createUbicacion(ColectivoRecorrido cr, String latitud, String longitud) {


        GeometryFactory geomFactory = new GeometryFactory();
        Point point = geomFactory.createPoint(new Coordinate(Double.parseDouble(latitud), Double.parseDouble(longitud)));
        
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setColectivoRecorrido(cr);
        ubicacion.setFecha(Calendar.getInstance());
        ubicacion.setCoordenada(point);

        
        return this.repoUbicacion.save( ubicacion );
    }

    @Override
    public Notificacion createNotificacion(ColectivoRecorrido cr, String latitud, String longitud,
            String segundosDetenidoStr, Boolean notificacionEstaActiva) {

        GeometryFactory geometryFactory = new GeometryFactory();
        Point coordenadasNotificacion = geometryFactory.createPoint(new Coordinate(Double.parseDouble(latitud), Double.parseDouble(longitud)));
        Notificacion notificacion = new Notificacion();
        notificacion.setFecha(Calendar.getInstance());
        notificacion.setColectivoRecorrido(cr);
        notificacion.setDescripcion("Unidad detenida por "+segundosDetenidoStr+" minutos");
        notificacion.setActiva(notificacionEstaActiva);
        notificacion.setTipo("DETENIDO");
        notificacion.setCoordenadas(coordenadasNotificacion);
        this.repoNotificacion.save(notificacion);
    return notificacion;
    }


    // para app colectivo
    @Override
    public Notificacion updateNotificacion(ColectivoRecorrido cr, String latitud, String longitud,
            String segundosDetenidoStr, Boolean notificacionEstaActiva) {

                 try {
            Notificacion notificacion = repoNotificacion.findNotificacionColeDetenidoActiva(cr.getId());
            if (notificacion != null) {
                notificacion.setActiva(notificacionEstaActiva);
                GeometryFactory geometryFactory = new GeometryFactory();
                Point coordenadasNotificacion = geometryFactory.createPoint(new Coordinate(Double.parseDouble(latitud), Double.parseDouble(longitud)));
                notificacion.setCoordenadas(coordenadasNotificacion);
                notificacion.setDescripcion("Unidad detenida por "+ segundosDetenidoStr +" minutos");
                return this.repoNotificacion.save(notificacion);
            }
            return null;
        } catch (Exception ex) {
            System.out.println("++++++ ERROR actualizando notificacion " + ex);
            return null;
        }
    }

    @Override
    public Notificacion finNotificacionColeDetenido(ColectivoRecorrido cr, String latitud, String longitud,
            String segundosDetenidoStr, Boolean notificacionEstaActiva) {
          try {

            Notificacion notificacion = repoNotificacion.findNotificacionColeDetenidoActiva(cr.getId());

            if (notificacion != null) {
                notificacion.setActiva(notificacionEstaActiva);
                GeometryFactory geometryFactory = new GeometryFactory();
                Point coordenadasNotificacion = geometryFactory.createPoint(new Coordinate(Double.parseDouble(latitud), Double.parseDouble(longitud)));
                notificacion.setCoordenadas(coordenadasNotificacion);
                notificacion.setDescripcion("Unidad detenida por "+ segundosDetenidoStr +" minutos");
                return this.repoNotificacion.save(notificacion);
            }
            return null;
        } catch (Exception ex) {
            System.out.println("++++++ ERROR finalizando notificacion " + ex);
            return null;
        }
    }


    // para app pasajero
    public List<ColectivoRecorrido> findAllColectivosRecorridoActivos(long idLinea, long idRecorrido) {
        try{
            return repoColRec.findAllColectivosRecorridoActivos(idLinea, idRecorrido);
        }catch (Exception ex){
            System.out.println("+++++++++ no se encontraron colectivos recorrido activos para esa linea");
            return null;
        }
    }



    // para pantalla monitoreo unidad
    @Override
    public ColectivoRecorrido detenerColectivoRecorrido(long idColRec, long idLinea, boolean disabled){
        ColectivoRecorrido colRec = repoColRec.findColectivoRecorridoEnTransito(idColRec, idLinea);
         if (colRec != null) {
            colRec.setTransito(disabled);
            colRec.getColectivo().setEnCirculacion(disabled);
            repoColRec.save(colRec);
        }
        return colRec;
    }

    @Override
    public ColectivoRecorrido detenerColectivoRecorridoByDenom(String denomLinea, String unidad, String denomRecorrido, boolean disabled){
        ColectivoRecorrido colRec = repoColRec.findColectivoRecorrido(denomLinea, denomRecorrido, unidad );
         if (colRec != null) {
            colRec.setTransito(disabled);
            colRec.getColectivo().setEnCirculacion(disabled);
            repoColRec.save(colRec);
        }
        return colRec;
    }

    @Override
    public List<Notificacion> findNotificacionesActivas(){
      try{
            return repoNotificacion.findNotificacionesActivas();
        }catch (Exception ex){
            System.out.println("+++++++++ no se encontraron colectivos recorrido activos para esa linea");
            return null;
        }
    }



}
