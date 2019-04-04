package com.diancu.webserver.httpapi;

import java.nio.charset.StandardCharsets;

public interface HttpConfiguration {
    int getWorkerThreads();

    int getServerPort();

    int getSoTimeout();

    int getMaxHeaderLineLength();

    default String getEncoding() {
        return StandardCharsets.UTF_8.name();
    }
}
