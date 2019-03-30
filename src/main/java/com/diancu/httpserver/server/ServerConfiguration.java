package com.diancu.httpserver.server;

import java.io.File;

/**
 * Class that contains config server properties
 */
public class ServerConfiguration {

    private final String serverHost;
    private final int serverPort;
    private final File rootFolder;
    private int workerThreads;

    public ServerConfiguration(File serverRoot) {
        serverHost = "localhost";
        serverPort = 80;
        rootFolder = serverRoot;
        this.workerThreads = 30;
    }


    public String getServerHost() {
        return serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    public File getRootFolder() {
        return rootFolder;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }
}
