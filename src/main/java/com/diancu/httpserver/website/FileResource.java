package com.diancu.httpserver.website;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileResource implements WebResource {
    private static Map<String, String> contentTypes = new HashMap<>();

    static {
        contentTypes.put("txt", "text/plain");
        contentTypes.put("html", "text/html");
        contentTypes.put("jpg", "image/jpeg");
    }

    private final File sourceFile;

    public FileResource(File sourceFile) {

        this.sourceFile = sourceFile;
    }

    @Override
    public String getContentType() {
        return isFolder() ? "text/html"
            : contentTypes.getOrDefault (
                    sourceFile.getName().substring(sourceFile.getName().lastIndexOf('.') + 1),
        "application/octetstream");
    }

    @Override
    public long getSize() {
        return isFolder() ? 0 :  sourceFile.length();
    }

    @Override
    public Path getPath() {
        return Paths.get(sourceFile.getAbsolutePath());
    }

    @Override
    public boolean isFolder() {
        return sourceFile.isDirectory();
    }
}
