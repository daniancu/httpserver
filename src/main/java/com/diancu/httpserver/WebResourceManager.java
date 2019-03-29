package com.diancu.httpserver;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class WebResourceManager {

    private final File rootFolder;

    public WebResourceManager(File rootFolder) {
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
}
