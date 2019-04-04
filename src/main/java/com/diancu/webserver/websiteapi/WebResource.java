package com.diancu.webserver.websiteapi;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

public interface WebResource {

    String getContentType();

    Path getPath();

    boolean isFolder();

    InputStream getInputStream() throws FileNotFoundException;
}
