package com.diancu.webserver.website;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class WebSite {

    private final File rootFolder;

    public WebSite(WebSiteConfiguration configuration) {
        File folder = new File(configuration.getRootFolderPath());
        if (folder.exists() && folder.isDirectory()) {
            log.info("Website root folder is {}", folder);
            this.rootFolder = folder;
        } else {
            throw new IllegalArgumentException("Root folder path does not exist or not a directory: " + configuration.getRootFolderPath());
        }
    }

    public WebResource locate(String resource) {
        log.info("Locating resource '{}'", resource);
        File requestedFile = new File(rootFolder, resource);
        log.debug("requestedFile: {}", requestedFile);

        if (requestedFile.exists()) {
            return requestedFile.isDirectory()
                    ? new FolderResource(requestedFile, rootFolder.toPath().equals(requestedFile.toPath()))
                    : new FileResource(requestedFile);
        }

        log.info("Resource '{}' not found");
        return null;
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
