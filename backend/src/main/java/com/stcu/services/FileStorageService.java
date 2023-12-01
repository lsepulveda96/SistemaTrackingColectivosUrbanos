package com.stcu.services;

import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

public interface FileStorageService {
    
    public void init();

    public String save(MultipartFile file);

    public Resource load(String filename);

    public boolean delete(String filename);

    public void deleteAll();

    public Stream<Path> loadAll();
}
