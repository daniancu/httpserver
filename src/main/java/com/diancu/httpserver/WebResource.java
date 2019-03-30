package com.diancu.httpserver;

import java.nio.file.Path;

public interface WebResource {

    String getContentType();

    long getSize();

    Path getPath();
}
