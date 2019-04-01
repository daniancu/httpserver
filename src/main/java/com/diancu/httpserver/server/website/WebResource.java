package com.diancu.httpserver.server.website;

import java.nio.file.Path;

public interface WebResource {

    String getContentType();

    long getSize();

    Path getPath();

    boolean isFolder();
}
