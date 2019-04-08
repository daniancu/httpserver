package com.diancu.webserver.websiteapi;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * WebResource implementation for a static file
 */
@Slf4j
public class FileResource implements WebResource {

    private final File sourceFile;

    public FileResource(File sourceFile) {

        this.sourceFile = sourceFile;
    }

    @Override
    public String getContentType() {
        try {
            return Files.probeContentType(sourceFile.toPath());
        } catch (IOException e) {
            log.error("error getting content type for file: {}", sourceFile);
            return "application/octet-stream";
        }
    }

    @Override
    public Path getPath() {
        return Paths.get(sourceFile.getAbsolutePath());
    }

    @Override
    public boolean isFolder() {
        return false;
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(sourceFile);
    }


}
