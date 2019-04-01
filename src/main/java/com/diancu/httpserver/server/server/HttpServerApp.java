package com.diancu.httpserver.server.server;

import com.diancu.httpserver.server.http.HttpServer;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;

@Slf4j
public class HttpServerApp {
    public static void main(String[] args) {
        log.info("Starting http server...");
        try {
            URL embeddedSiteLocation = HttpServer.class.getClassLoader().getResource("site");
            if (embeddedSiteLocation != null) {

                ServerConfiguration config = new ServerConfiguration(new File(embeddedSiteLocation.getPath()));

                new HttpServer(config).start().join();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
