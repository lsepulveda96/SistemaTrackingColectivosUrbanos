package com.stcu.controllers;

import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.stcu.model.Colectivo;
import com.stcu.model.Documento;
import com.stcu.model.FileInfo;
import com.stcu.services.ColectivoServiceImp;
import com.stcu.services.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class ColectivoController {

    @Autowired
    ColectivoServiceImp service;
    @Autowired
    FileStorageService storageService;

    private static final Logger log = Logger.getLogger(ColectivoController.class.getName());

    /**
     * Recupera listado de colectivos completa.
     * 
     * @return
     */
    @GetMapping("/colectivos")
    public String findAllColectivos() {
        log.info("*** findAllColectivos");
        List<Colectivo> list = service.getAllColectivos();
        log.info("*** Colectivos length: " + list.size());
        Response<List<Colectivo>> response = new Response<List<Colectivo>>(false, 200, "Listado de colectivos", list);
        return Mapper.getResponseAsJson(response);
    }

    /**
     * Busca y retorna un objeto colectivo por su id.
     * 
     * @param id
     * @return
     */
    @GetMapping("/colectivo/{id}")
    public String findColectivo(@PathVariable long id) {
        log.info("*** findColectivo: " + id);
        Colectivo col = service.getColectivo(id);
        Response<Colectivo> response;
        if (col != null) {
            log.info("*** colectivo: " + col.getUnidad());
            response = new Response<Colectivo>(false, 200, "Colectivo id " + id, col);
        } else {
            log.warning("*** No se encontro colectivo " + id);
            response = new Response<Colectivo>(true, 400, "No se encontro colectivo id = " + id, null);
        }
        return Mapper.getResponseAsJson(response);
    }

    /**
     * Registra un nuevo colectivo
     * 
     * @param col: datos del colectivo a registrar
     * @return
     */
    @PostMapping("/colectivos")
    public String saveColectivo(@RequestBody Colectivo col) {
        log.info("*** saveColectivo: " + col.getUnidad());
        Colectivo colectivo = service.saveColectivo(col);
        Response<Colectivo> response;
        if (colectivo != null) {
            log.info("*** Colectivo registrado: " + col.getId());
            response = new Response<Colectivo>(false, 200, "Nuevo Colectivo registrado", colectivo);
        } else {
            log.warning("*** No se pudo registrar colectivo");
            response = new Response<Colectivo>(true, 400, "No se pudo registrar nuevo colectivo", null);
        }
        return Mapper.getResponseAsJson(response);
    }

    /**
     * Actualiza los datos de un colectivo registrado
     * 
     * @param id:  del colectivo a actualizar
     * @param col: nuevos datos a actualizar.
     * @return
     */
    @PutMapping("/colectivo/{id}")
    public String updateColectivo(@PathVariable long id, @RequestBody Colectivo col) {
        log.info("*** updateColectivo: " + id);
        Colectivo colectivo = service.updateColectivo(id, col);
        Response<Colectivo> response;
        if (colectivo != null) {
            log.info("*** Colectivo actualizado " + id);
            response = new Response<Colectivo>(false, 200, "Colectivo " + id + " actualizado", colectivo);
        } else {
            log.warning("*** No se puedo actualizar colectivo " + id);
            response = new Response<Colectivo>(true, 400, "No se pudo actualizar colectivo " + id, null);
        }
        return Mapper.getResponseAsJson(response);
    }

    /**
     * Setea la baja de un colectivo.
     * 
     * @param id
     * @return
     */
    @GetMapping("/colectivo/baja/{id}")
    public String bajaColectivo(@PathVariable long id) {
        log.info("*** bajaColectivo: " + id);
        boolean stat = service.bajaColectivo(id);
        if (stat)
            log.info("*** Colectivo " + id + " se dio de baja");
        else
            log.warning("*** No se pudo dar de baja colectivo " + id);
        Response<Boolean> resp = new Response<Boolean>(true, 200,
                stat ? "Unidad " + id + " de baja" : "No se pudo dar de baja unidad " + id, stat);
        return Mapper.getResponseAsJson(resp);
    }

    @PostMapping("/files/doc/upload/{id}")
    public ResponseEntity<Response<String>> uploadDocFile(
            @PathVariable long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("vence") String vence,
            @RequestParam("vencimiento") String vencimiento,
            @RequestParam("file") MultipartFile file) {

        String namefile = file.getOriginalFilename();
        log.info("*** upload Doc File: " + namefile +
                ", nombre: " + nombre +
                ", vence: " + "true".equalsIgnoreCase(vence) +
                ", vencimiento: " + vencimiento);

        Calendar venc;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dvenc = sdf.parse(vencimiento);
            venc = Calendar.getInstance();
            venc.setTime(dvenc);
        } catch (ParseException e) {
            venc = null;
        }

        Response<String> response;
        try {
            String fn = storageService.save(file);
            Documento doc = new Documento(nombre, namefile, fn, "true".equalsIgnoreCase(vence), venc);
            this.service.saveDocumento(id, doc);

            log.info("*** Archivo documento cargado exitosamente");

            response = new Response<String>(false, 200, "Archivo docuemnto cargado exitosamente", fn);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception ex) {
            log.warning("*** No se pudo cargar archivo documento, ex: " + ex.getMessage());
            response = new Response<String>(true, 300,
                    "No se puedo cargar archivo documento " + file.getOriginalFilename(),
                    null);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
        }
    }

    @PostMapping("/files/doc/updatefile/{id}")
    public ResponseEntity<Response<String>> updateDocFile(
            @PathVariable long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("vence") String vence,
            @RequestParam("vencimiento") String vencimiento,
            @RequestParam("file") MultipartFile file) {

        String namefile = file != null ? file.getOriginalFilename() : null;
        log.info("*** update Doc File: " + namefile +
                ", nombre: " + nombre +
                ", vence: " + "true".equalsIgnoreCase(vence) +
                ", vencimiento: " + vencimiento);

        Calendar venc;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dvenc = sdf.parse(vencimiento);
            venc = Calendar.getInstance();
            venc.setTime(dvenc);
        } catch (ParseException e) {
            venc = null;
        }

        Response<String> response;
        try {
            Documento doc = service.getDocumento(id);
            if (doc != null) {
                String fn = doc.getPathfile();
                storageService.delete(fn); // eliminar archivo anterior
                fn = storageService.save(file); // guardar nuevo archivo.

                doc.setNamefile(namefile);
                doc.setNombre(nombre);
                doc.setPathfile(fn);
                doc.setVence("true".equalsIgnoreCase(vence));
                doc.setVencimiento(venc);
                this.service.updateDocumento(id, doc);
                log.info("*** Archivo documento actualizado");

                response = new Response<String>(false, 200, "Archivo docuemnto actualizado exitosamente",
                        doc.getNamefile());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                log.warning("*** Archivo documento " + id + " no se encontro");

                response = new Response<String>(true, 300,
                        "No se encontro archivo documento " + id,
                        null);
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
            }
        } catch (Exception ex) {
            log.warning("*** No se pudo cargar archivo documento, ex: " + ex.getMessage());
            response = new Response<String>(true, 300,
                    "No se puedo cargar archivo documento " + id,
                    null);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
        }
    }

    @PutMapping("/files/doc/updatedata/{id}")
    public String updateDocData(@PathVariable long id, @RequestBody Documento doc) {
        log.info("*** update Doc data File: " + id);

        Response<Documento> response;
        Documento udoc = service.updateDocumento(id, doc);
        if (udoc != null) {
            log.info("*** documento actualizado " + id);
            response = new Response<Documento>(false, 200, "Documento " + id + " actualizado", udoc);
        } else {
            log.warning("*** No se puedo actualizar documento " + id);
            response = new Response<Documento>(true, 400, "No se pudo actualizar documento " + id, null);
        }
        return Mapper.getResponseAsJson(response);
    }

    /**
     * Busca y realiza la descarga de un archivo en el sistema de archivos del
     * servidor hacia un cliente.
     * 
     * @param filename
     * @return
     */
    @GetMapping("/files/doc/download/{filename:.+}")
    public ResponseEntity<Resource> getDocFile(@PathVariable String filename) {
        log.info("*** getFile: " + filename);
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    /**
     * Elimina un archivo del sistema de archivos.
     * 
     * @param filename
     * @return
     */
    @DeleteMapping("/files/doc/delete/{id}")
    public ResponseEntity<Response> deleteDocFile(@PathVariable long id) {
        log.info("*** delete Doc File: " + id);
        String message = "";
        Response<String> response;
        try {
            Documento doc = service.getDocumento(id);
            boolean existed = storageService.delete(doc.getPathfile());
            if (existed) {
                log.info("*** archivo eliminado: " + doc.getPathfile());
                service.removeDocumento(id);
                message = "Archivo borrado" + doc.getNamefile() + " exitosamente";
                // return ResponseEntity.ok().body( message );
                response = new Response<String>(false, 200, message, message);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                log.info("*** no se encontro archivo  " + doc.getNamefile());
                message = "El archivo " + doc.getNamefile() + " no se encontro!";
                // return ResponseEntity.ok().body( message );
                response = new Response<String>(true, 300, message, message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception ex) {
            log.warning("*** Error intentando eliminar archivo documento " + id + ", ex: " + ex.getMessage());
            message = "No se pudo eliminar el archivo documento " + id;
            // message = "Could not delete the file " + filename + ", Error: " +
            // ex.getMessage();
            response = new Response<String>(true, 300, message, message);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Realiza la carga de un archivo recibido en el sistema de archivos del
     * servidor.
     * 
     * @param file
     * @return
     */
    @PostMapping("/files/image/upload/{id}")
    public ResponseEntity<Response<String>> uploadImgFile(
            @PathVariable long id,
            @RequestParam("file") MultipartFile file) {
        log.info("*** uploadFile: " + file.getOriginalFilename());
        Response<String> response;
        try {
            String fn = storageService.save(file);
            this.service.saveImage(id, fn);
            log.info("*** Archivo cargado exitosamente");
            response = new Response<String>(false, 200, "Archivo cargado exitosamente", fn);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception ex) {
            log.warning("*** No se pudo cargar archivo, ex: " + ex.getMessage());
            response = new Response<String>(true, 300, "No se puedo cargar archivo " + file.getOriginalFilename(),
                    null);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
        }
    }

    /**
     * Elimina un archivo del sistema de archivos.
     * 
     * @param filename
     * @return
     */
    @DeleteMapping("/files/image/delete/{filename:.+}")
    public ResponseEntity<Response> deleteImgFile(@PathVariable String filename) {
        log.info("*** deleteFile: " + filename);
        String message = "";
        Response<String> response;
        try {
            boolean existed = storageService.delete(filename);
            if (existed) {
                log.info("*** archivo eliminado: " + filename);
                message = "Archivo borrado" + filename + " exitosamente";
                // return ResponseEntity.ok().body( message );
                response = new Response<String>(false, 200, message, message);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                log.info("*** no se encontro archivo  " + filename);
                message = "El archivo " + filename + " no se encontro!";
                // return ResponseEntity.ok().body( message );
                response = new Response<String>(true, 300, message, message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception ex) {
            log.warning("*** Error intentando eliminar archivo " + filename + ", ex: " + ex.getMessage());
            message = "No se pudo eliminar el archivo " + filename;
            // message = "Could not delete the file " + filename + ", Error: " +
            // ex.getMessage();
            response = new Response<String>(true, 300, message, message);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/colectivos/docs/vencimientos")
    public String getDocsVencimientos() {
        log.info("*** getDocsVencimientos");
        /* List<Documento> list = service.getDocsVencidosProximoVencer();
        log.info("*** documentos length: " + list.size());
        Response<List<Documento>> response = new Response<List<Documento>>(false, 200, "Listado de documentos vencidos o proximos a vencer", list); */
        List<Colectivo> list = service.getColectivosDocsVencimiento();
        log.info("*** colectivos documentos length: " + list.size());
        Response<List<Colectivo>> response = new Response<List<Colectivo>>(false, 200, "Listado de documentos vencidos o proximos a vencer", list);
        return Mapper.getResponseAsJson(response);
    }
}
