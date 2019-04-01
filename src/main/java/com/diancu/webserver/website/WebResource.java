package com.diancu.webserver.website;

import java.nio.file.Path;

public interface WebResource {

    String getContentType();

    long getSize();

    Path getPath();

    boolean isFolder();
}
