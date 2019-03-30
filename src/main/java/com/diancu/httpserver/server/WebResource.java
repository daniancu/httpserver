package com.diancu.httpserver.server;

import java.nio.file.Path;

public interface WebResource {

    String getContentType();

    long getSize();

    Path getPath();
}
