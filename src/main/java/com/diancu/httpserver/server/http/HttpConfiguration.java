package com.diancu.httpserver.server.http;

import java.io.File;

public interface HttpConfiguration {
    int getWorkerThreads();

    File getRootFolder();

    int getServerPort();

    int getSoTimeout();

    int getMaxStatusLineLength();
}
