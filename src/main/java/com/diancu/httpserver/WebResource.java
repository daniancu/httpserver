package com.diancu.httpserver;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

public interface WebResource {

    String getContentType();

    long getSize();

    InputStream getInputStream() throws FileNotFoundException;

    Path getPath();
}
