package com.diancu.webserver.website;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public long getSize() {
        return sourceFile.length();
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
