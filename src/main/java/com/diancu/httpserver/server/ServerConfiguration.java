package com.diancu.httpserver.server;

import com.diancu.httpserver.http.HttpConfiguration;
import com.diancu.httpserver.website.WebSiteConfiguration;
import lombok.Data;

/**
 * Class that contains config server properties
 */
@Data
public class ServerConfiguration implements HttpConfiguration, WebSiteConfiguration {

    private String serverHost;
    private int serverPort;
    private String rootFolderPath;
    private int workerThreads;
    private int maxStatusLineLength;
    private int soTimeout;

    public ServerConfiguration(String serverRoot) {
        serverHost = "localhost";
        serverPort = 80;
        rootFolderPath = serverRoot;
        workerThreads = 30;
        maxStatusLineLength = 300;
        soTimeout = 0;
    }

}
