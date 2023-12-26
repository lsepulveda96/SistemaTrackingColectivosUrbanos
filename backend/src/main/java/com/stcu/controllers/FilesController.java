package com.stcu.controllers;

import java.util.stream.Collectors;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.stcu.model.FileInfo;
import com.stcu.services.FileStorageService;

@RestController
@RequestMapping("/api/files")
public class FilesController {

    @Autowired
    FileStorageService storageService;

    private static final Logger log = Logger.getLogger(FilesController.class.getName());

    /**
     * Realiza la carga de un archivo recibido en el sistema de archivos del
     * servidor.
     * 
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity<Response<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        log.info("*** uploadFile: " + file.getOriginalFilename());
        Response<String> response;
        try {
            String fn = storageService.save(file);
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
     * Recupera lista de archivos en el directorio.
     * 
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        log.info("*** getListFiles: ");
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();
            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    /**
     * Busca y realiza la descarga de un archivo en el sistema de archivos del
     * servidor hacia un cliente.
     * 
     * @param filename
     * @return
     */
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
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
    @DeleteMapping("/delete/{filename:.+}")
    public ResponseEntity<Response> deleteFile(@PathVariable String filename) {
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
}
