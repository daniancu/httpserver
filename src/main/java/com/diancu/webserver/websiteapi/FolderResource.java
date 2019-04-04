package com.diancu.webserver.websiteapi;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FolderResource implements WebResource {

    private final File sourceFile;
    private final boolean isRoot;
    private final FolderContentRenderer renderer;

    public FolderResource(File sourceFile, boolean isRoot) {
        this.sourceFile = sourceFile;
        this.isRoot = isRoot;
        this.renderer = new FolderContentRenderer() {};
    }

    @Override
    public String getContentType() {
        return renderer.getContentType();
    }

    @Override
    public Path getPath() {
        return Paths.get(sourceFile.getAbsolutePath());
    }

    @Override
    public boolean isFolder() {
        return true;
    }

    @Override
    public InputStream getInputStream() {
        log.debug("Generating directory indexing page for folder: {}", sourceFile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.renderFolderContent(this, outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    /** package level methods, used by FolderContentRenderer to display folder content */
    boolean isRoot() {
        return isRoot;
    }

    File[] listFiles() {
        return sourceFile.listFiles();
    }

    File getSourceFile() {
        return sourceFile;
    }
}
