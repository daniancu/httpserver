package com.diancu.httpserver.server;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileResource implements WebResource {
    private final File sourceFile;

    public FileResource(File sourceFile) {

        this.sourceFile = sourceFile;
    }

    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    public long getSize() {
        return sourceFile.length();
    }

    @Override
    public Path getPath() {
        return Paths.get(sourceFile.getAbsolutePath());
    }
}
