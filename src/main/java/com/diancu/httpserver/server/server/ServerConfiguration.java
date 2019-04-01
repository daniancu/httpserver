package com.diancu.httpserver.server.server;

import com.diancu.httpserver.server.http.HttpConfiguration;
import lombok.Data;

import java.io.File;

/**
 * Class that contains config server properties
 */
@Data
public class ServerConfiguration implements HttpConfiguration {

    private String serverHost;
    private int serverPort;
    private File rootFolder;
    private int workerThreads;
    private int maxStatusLineLength;
    private int soTimeout;

    public ServerConfiguration(File serverRoot) {
        serverHost = "localhost";
        serverPort = 80;
        rootFolder = serverRoot;
        workerThreads = 30;
        maxStatusLineLength = 300;
        soTimeout = 0;
    }

}
