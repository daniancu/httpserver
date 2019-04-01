package com.diancu.httpserver.server;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class WebSite {

    private final File rootFolder;

    public WebSite(File rootFolder) {
        if (rootFolder != null && rootFolder.isDirectory()) {
            this.rootFolder = rootFolder;
        } else {
            throw new IllegalArgumentException("Root folders is not a directory: " + rootFolder);
        }
    }

    public WebResource locate(String resource) {
        log.info("Locating resource '{}'", resource);
        File requestedFile = new File(rootFolder, resource);
        log.debug("requestedFile: {}", requestedFile);

        if (requestedFile.exists() && requestedFile.isFile()) {
            return new FileResource(requestedFile);
        }

        log.info("Resource '{}' not found");
        return null;
    }

    public boolean exists(String resource) {
        log.debug("Checking if resource {} exists...", resource);
        File file = new File(rootFolder, resource);
        log.debug("Resource location is {}", file.getAbsolutePath());
        return file.exists();
    }

    public boolean create(String resourceUri, File externalFile) {
        File file = new File(rootFolder, resourceUri);
        log.debug("Creating resource {}...", file);
        return externalFile.renameTo(file);

    }

    public boolean delete(String resourceUri) throws IOException {
        WebResource res = locate(resourceUri);
        if (res.isFolder()) {
            return false;
        } else {
             return Files.deleteIfExists(res.getPath());
        }
    }
}
