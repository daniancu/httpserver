package com.diancu.httpserver.website;

import java.nio.file.Path;

public interface WebResource {

    String getContentType();

    long getSize();

    Path getPath();

    boolean isFolder();
}
