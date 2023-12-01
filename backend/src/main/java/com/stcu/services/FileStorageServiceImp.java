package com.stcu.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.management.RuntimeErrorException;

import java.time.Instant;

@Service
public class FileStorageServiceImp implements FileStorageService {
    
    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        }
        catch(IOException ex) {
            throw new RuntimeException("Could not initialize folder to upload!");
        }
    }

    @Override
    public String save(MultipartFile file ) {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            String name_file = currentTimeMillis+"-"+file.getOriginalFilename();
            Files.copy(file.getInputStream(), this.root.resolve(name_file));
            return name_file;
        }
        catch(Exception ex ) {
            if (ex instanceof FileAlreadyExistsException)
                throw new RuntimeException("A file of that name already exists.");
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new RuntimeException("Could not read the file!");
            }
        }
        catch(MalformedURLException ex) {
            throw new RuntimeException("Error: " + ex.getMessage());
        }
    }

    @Override
    public boolean delete(String filename) {
        try {
            Path file = root.resolve(filename);
            return Files.deleteIfExists(file);
        }
        catch(IOException ex) {
            throw new RuntimeException("Error: " + ex.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root,1).filter( path -> !path.equals(this.root)).map(this.root::relativize);            
        }
        catch(IOException ex) {
            throw new RuntimeException("Could not load the files!");
        }
    }    
}
