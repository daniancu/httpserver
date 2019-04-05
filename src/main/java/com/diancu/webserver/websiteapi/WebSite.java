package com.diancu.webserver.websiteapi;

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

    /**
     * Locates a resource in the website
     *
     * @param resourceUri relative path to the resource
     * @return resource object or null if resource does not exist
     */
    public WebResource locate(String resourceUri) {
        log.info("Locating resource '{}'", resourceUri);
        File requestedFile = new File(rootFolder, resourceUri);
        log.debug("requestedFile: {}", requestedFile);

        if (requestedFile.exists()) {
            return requestedFile.isDirectory()
                    ? new FolderResource(requestedFile, rootFolder.toPath().equals(requestedFile.toPath()))
                    : new FileResource(requestedFile);
        }

        log.info("Resource '{}' not found", resourceUri);
        return null;
    }

    public boolean create(String resourceUri, File externalFile) {
        File file = new File(rootFolder, resourceUri);
        log.debug("Creating resource {}...", file);
        return externalFile.renameTo(file);

    }

    /**
     * Deletes a resource identified by an resourceUri. It does not support folder
     *
     * @param resourceUri resource identifier
     * @return true if deleted, false otherwise
     * @throws WebResourceNotFoundException resource could not be located
     */
    public boolean delete(String resourceUri) throws WebsiteException {
        WebResource res = locate(resourceUri);
        if (res != null) {
            if (!res.isFolder()) {
                try {
                    log.debug("Deleting file {} ...", res.getPath());
                    return Files.deleteIfExists(res.getPath());
                } catch (IOException e) {
                    throw new WebsiteException("Failed to delete resource", e);
                }
            } else {
                throw new WebsiteException("");
            }
        } else {
            throw new WebResourceNotFoundException(resourceUri);
        }
    }
}
