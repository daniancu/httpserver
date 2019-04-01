package com.diancu.httpserver.http;

import java.io.File;

public interface HttpConfiguration {
    int getWorkerThreads();

    int getServerPort();

    int getSoTimeout();

    int getMaxStatusLineLength();
}
