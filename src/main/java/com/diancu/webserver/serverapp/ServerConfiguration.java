package com.diancu.webserver.serverapp;

import com.diancu.webserver.httpapi.HttpConfiguration;
import com.diancu.webserver.websiteapi.WebSiteConfiguration;
import lombok.Data;

/**
 * Class that contains config serverapp properties
 */
@Data
public class ServerConfiguration implements HttpConfiguration, WebSiteConfiguration {

    private String serverHost;
    private int serverPort;
    private String rootFolderPath;
    private int workerThreads;
    private int maxHeaderLineLength;
    private int soTimeout;

    public ServerConfiguration(String serverRoot) {
        serverHost = "localhost";
        serverPort = 8001;
        rootFolderPath = serverRoot;
        workerThreads = 30;
        maxHeaderLineLength = 300;
        soTimeout = 0;
    }

}
