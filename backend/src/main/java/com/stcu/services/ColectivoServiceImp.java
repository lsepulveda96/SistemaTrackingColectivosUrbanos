package com.stcu.services;

import java.util.List;

import com.stcu.model.Colectivo;
import com.stcu.model.Documento;
import com.stcu.repository.ColectivoRepository;
import com.stcu.repository.DocumentoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ColectivoServiceImp implements ColectivoService {

    @Autowired
    private ColectivoRepository rep;
    @Autowired
    private DocumentoRepository repDoc;

    @Override
    public List<Colectivo> getAllColectivos() {
        List<Colectivo> cols = this.rep.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return cols;
    }

    @Override
    public Colectivo getColectivo(long id) {

        return this.rep.findById(id);
    }

    @Override
    public Colectivo saveColectivo(Colectivo col) {
        try {
            return this.rep.save(col);
        } catch (Exception ex) {
            System.out.println("++++++ ERROR guardano nuevo colectivo: " + ex);
            return null;
        }
    }

    @Override
    public Colectivo updateColectivo(long id, Colectivo col) {
        try {
            Colectivo colectivo = this.rep.findById(id);
            if (colectivo != null) {
                colectivo.setUnidad(col.getUnidad());
                colectivo.setMarca(col.getMarca());
                colectivo.setModelo(col.getModelo());
                colectivo.setCapacidad(col.getCapacidad());
                colectivo.setAnio(col.getAnio());
                colectivo.setPatente(col.getPatente());
                colectivo.setFechaCompra(col.getFechaCompra());
                colectivo.setEstado(col.getEstado());
                colectivo.setImgpath(col.getImgpath());
                return this.rep.save(colectivo);
            }
            return null;
        } catch (Exception ex) {
            System.out.println("++++++ ERROR actualizando colectivo " + ex);
            return null;
        }
    }

    @Override
    public boolean bajaColectivo(long id) {
        Colectivo col = this.rep.findById(id);
        if (col != null) {
            col.setEstado("BAJA");
            this.rep.save(col);
            return true;
        }
        return false;
    }


    @Override
    public Colectivo getColectivoByUnidad( String unidad ){
        return this.rep.findByUnidad(unidad);
    }

    @Override
    public List<Colectivo> getAllColectivosSinCircular() {
        List<Colectivo> cols = this.rep.findAllColectivosSinCircular();
        return cols;
    }

    @Override
    public Documento saveDocumento( long id, Documento doc) {
        Colectivo col = this.rep.findById(id);
        Documento ndoc = this.repDoc.save(doc);
        col.getDocumentos().add(ndoc);
        this.rep.save( col );
        return doc;
    }

    @Override
    public Documento updateDocumento(Documento doc) {
        try {
            Documento docu = this.repDoc.findById(doc.getId());
            if (docu != null) {
                docu.setNombre( doc.getNombre());
                docu.setNamefile(doc.getNamefile());
                docu.setPathfile(doc.getPathfile());
                docu.setVence(doc.isVence());
                docu.setVencimiento(doc.getVencimiento());
                return this.repDoc.save(docu);
            }
            return null;
        } catch (Exception ex) {
            System.out.println("++++++ ERROR actualizando documento " + ex);
            return null;
        }
    }

    @Override
    public boolean removeDocumento(long id) {
        this.repDoc.deleteById(id);
        return true;
    }

    @Override
    public Colectivo saveImage(long id, String path) {
        Colectivo col = this.rep.findById(id);
        col.setImgpath(path);
        this.rep.save( col );
        return col;
    }

    @Override
    public Documento getDocumento( long id ) {
        return this.repDoc.findById(id);
    }

}