package com.diancu.webserver.website;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

public interface WebResource {

    String getContentType();

    long getSize();

    Path getPath();

    boolean isFolder();

    InputStream getInputStream() throws FileNotFoundException;
}
