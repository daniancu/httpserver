package com.diancu.webserver.http;

public interface HttpConfiguration {
    int getWorkerThreads();

    int getServerPort();

    int getSoTimeout();

    int getMaxStatusLineLength();
}
