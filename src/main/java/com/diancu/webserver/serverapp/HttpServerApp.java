package com.diancu.webserver.serverapp;

import com.diancu.webserver.httpapi.HttpHandlers;
import com.diancu.webserver.httpapi.HttpServer;
import com.diancu.webserver.websiteapi.WebSite;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Basic implementation of a HTTP server application using httpapi and websiteapi
 * It serves a static html site included in the project resources on port 8001
 */
@Slf4j
public class HttpServerApp {

    public static void main(String[] args) {
        log.info("Starting http server...");
        try {
            //look for the site on the classpath output where mvn install should copy it
            URL embeddedSiteLocation = HttpServer.class.getClassLoader().getResource("site");
            if (embeddedSiteLocation != null) {
                //uses the default config values. in a future iteration the config could be read from a config file
                ServerConfiguration config = new ServerConfiguration(embeddedSiteLocation.getPath());

                //wire the main components
                WebSite website = new WebSite(config);
                HttpHandlers handlers = new HttpHandlers(website);
                ExecutorService executor = getExecutorService(config);

                //start the server and wait for it
                new HttpServer(config, handlers, executor).start().join();

            } else {
                log.error("Could not locate embedded site");
            }
        } catch (Exception e) {
            log.error("Http serverapp error", e);
        }
    }

    private static ExecutorService getExecutorService(ServerConfiguration config) {
        AtomicInteger threadCount = new AtomicInteger(0);
        return Executors.newFixedThreadPool(config.getWorkerThreads(), r -> {
            Thread t = new Thread(r);
            t.setName("HttpServerThread-" + threadCount.incrementAndGet());
            return t;
        });
    }
}
