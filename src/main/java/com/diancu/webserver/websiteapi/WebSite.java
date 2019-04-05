package com.diancu.webserver.websiteapi;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

    public boolean add(String resourceUri, File externalFile) {
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
    public void delete(String resourceUri) throws WebsiteException {
        WebResource res = locate(resourceUri);
        if (res != null) {
            if (!res.isFolder()) {
                try {
                    log.debug("Deleting file {} ...", res.getPath());
                    Files.delete(res.getPath());
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

    public synchronized String addOrReplace(String resourceUri, File tmpFile) throws WebsiteException {
        WebResource existing = locate(resourceUri);
        if (existing == null) {
            File dest = new File(rootFolder, resourceUri);
            log.debug("Adding resource {}...", dest.getPath());
            if (tmpFile.renameTo(dest)) {
                return "added";
            } else {
                throw new WebsiteException("Could not add resource " + resourceUri);
            }
        } else {
            if (!existing.isFolder()) {
                try {
                    Path existingPath = existing.getPath();
                    log.debug("Deleting resource {}...", existing.getPath());
                    Files.delete ( existing.getPath());
                    log.debug("Adding resource {} from {}...", existing.getPath(), tmpFile.getPath());
                    if (tmpFile.renameTo(existingPath.toFile())){
                        return "replaced";
                    } else {
                        throw new WebsiteException("Could not resource file" + resourceUri );
                    }
                } catch (IOException e) {
                    throw new WebsiteException("Could not delete resource", e);
                }
            } else {
                throw new WebsiteException("Could not replace folder with file");
            }
        }
    }
}
