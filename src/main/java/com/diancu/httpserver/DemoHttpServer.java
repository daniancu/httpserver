package com.diancu.httpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DemoHttpServer {
    private final ServerConfiguration config;
    private Executor executor;

    public DemoHttpServer(ServerConfiguration config) {
        this.config = config;
        this.executor = Executors.newFixedThreadPool(config.getWorkerThreads(), r -> {
            Thread t = new Thread(r);
            t.setName("HttpHandlerThread");
            return t;
        });

    }

    public void start() {
        try {
            ServerSocket serverConnect = new ServerSocket(config.getServerPort());
            System.out.println("Server started.\nListening for connections on port : " + config.getServerPort() + " ...\n");

            // we listen until user halts server execution
            while (true) {
                Socket socket = serverConnect.accept();

                CompletableFuture.runAsync(() -> {

                }, executor);

            }

        } catch (IOException e) {
            System.err.println("Server Connection error : " + e.getMessage());
        }
    }


}
