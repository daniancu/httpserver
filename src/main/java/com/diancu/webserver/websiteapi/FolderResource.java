package com.diancu.webserver.websiteapi;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Getter
public class FolderResource implements WebResource {

    private final File sourceFile;
    private final boolean isRoot;
    private final FolderContentRenderer renderer;

    public FolderResource(File sourceFile, boolean isRoot) {
        this.sourceFile = sourceFile;
        this.isRoot = isRoot;
        //we use the default renderer that displays the folder content as a HTML page
        //in the future we could inject it to support other representations,e.g JSON
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

    public File[] listFiles() {
        return sourceFile.listFiles();
    }
}
