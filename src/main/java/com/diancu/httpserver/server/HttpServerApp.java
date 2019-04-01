package com.diancu.httpserver.server;

import com.diancu.httpserver.http.HttpHandlers;
import com.diancu.httpserver.http.HttpServer;
import com.diancu.httpserver.website.WebSite;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class HttpServerApp {
    public static void main(String[] args) {
        log.info("Starting http server...");
        try {
            URL embeddedSiteLocation = HttpServer.class.getClassLoader().getResource("site");
            if (embeddedSiteLocation != null) {

                ServerConfiguration config = new ServerConfiguration(embeddedSiteLocation.getPath());

                WebSite website = new WebSite(config);
                HttpHandlers handlers = new HttpHandlers(website);
                ExecutorService executor = getExecutorService(config);
                //start server and wait
                new HttpServer(config, handlers, executor).start().join();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static ExecutorService getExecutorService(ServerConfiguration config) {
        AtomicInteger threadCount = new AtomicInteger(0);
        return Executors.newFixedThreadPool(config.getWorkerThreads(), r -> {
            Thread t = new Thread(r);
            t.setName("DemoHttpServerThread-" + threadCount.incrementAndGet());
            return t;
        });
    }
}
