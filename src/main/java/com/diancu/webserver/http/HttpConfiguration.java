package com.diancu.webserver.http;

import java.nio.charset.StandardCharsets;

public interface HttpConfiguration {
    int getWorkerThreads();

    int getServerPort();

    int getSoTimeout();

    int getMaxStatusLineLength();

    default String getEncoding() {
        return StandardCharsets.UTF_8.name();
    }
}
