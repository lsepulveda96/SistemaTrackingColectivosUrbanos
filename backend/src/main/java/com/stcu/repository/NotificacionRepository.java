package com.stcu.repository;
import java.util.List;

import com.stcu.model.Notificacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findAll();

    Notificacion findById(long id);

    @Query("SELECT notif FROM Notificacion notif WHERE notif.activa is TRUE AND notif.tipo LIKE 'DESVIADO' AND notif.colectivoRecorrido.id = ?1")
    Notificacion findDesvioActivo(long idcr);

    // para app colectivo
    @Query("SELECT notif FROM Notificacion notif WHERE notif.activa is TRUE AND notif.tipo LIKE 'DETENIDO' AND notif.colectivoRecorrido.id = ?1")
    Notificacion findNotificacionColeDetenidoActiva(long idcr);

    // para pantalla notificaciones-avisos activas
    @Query("SELECT notif FROM Notificacion notif WHERE notif.activa IS TRUE")
    public List<Notificacion> findNotificacionesActivas();

}
