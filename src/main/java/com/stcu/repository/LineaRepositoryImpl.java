package com.stcu.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.stcu.model.Parada;
import com.stcu.model.Recorrido;
import com.stcu.model.ParadaRecorrido;

public class LineaRepositoryImpl implements LineaRepositoryCustom {

    @PersistenceContext 
    EntityManager entityManager;
    
    @Override
    public List<Recorrido> findAllRecorridos(long id) {
        String strquery = "SELECT rec FROM Recorrido rec WHERE rec.linea.id = :idlinea";
        Query query =  entityManager.createQuery( strquery, Recorrido.class );
        query.setParameter( "idlinea", id );

        @SuppressWarnings( "unchecked")
        List<Recorrido> result = query.getResultList();
        return result;
    }

    @Override
    public Recorrido findRecorridoActual(long id) {
        String sql = "SELECT rec FROM Recorrido rec WHERE rec.linea.id = :idlinea AND rec.activo = true";
        Query query = entityManager.createQuery( sql, Recorrido.class );
        query.setParameter( "idlinea", id );

        try {
            return (Recorrido)query.getSingleResult();
        }
        catch(NoResultException ex ) {
            return null;
        }
    }

    @Override
    public List<Parada> findParadas(long id) {
        String strquery = "SELECT pr FROM ParadaRecorrido pr WHERE pr.recorrido.id = :idrecorrido";
        Query query = entityManager.createQuery( strquery );
        query.setParameter( "idrecorrido", id );
        
        @SuppressWarnings("unchecked")
        List<ParadaRecorrido> paradasRecorrido = query.getResultList();
        
        List<Parada> paradas = new ArrayList<Parada>();
        if (paradasRecorrido != null) {
            for (ParadaRecorrido pr: paradasRecorrido)
                paradas.add( pr.getParada() );
        }
        
        return paradas;
    }
    
    
}
