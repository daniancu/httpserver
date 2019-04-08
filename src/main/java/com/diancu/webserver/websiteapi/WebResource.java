package com.diancu.webserver.websiteapi;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Defines a web resource in a website
 */
public interface WebResource {

    String getContentType();

    Path getPath();

    boolean isFolder();

    InputStream getInputStream() throws FileNotFoundException;
}
