package com.diancu.httpserver.server;


import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class HttpServer {
    private final ServerConfiguration config;
    private final AtomicInteger threadCount = new AtomicInteger(0);
    private final HttpHandlers handlers;
    private final ExecutorService executor;
    private AtomicBoolean active = new AtomicBoolean(true);
    private ServerSocket serverSocket;

    public HttpServer(ServerConfiguration config, HttpHandlers handlers, ExecutorService executor) {
        this.config = config;
        this.handlers = handlers;
        this.executor = executor;
    }

    public HttpServer(ServerConfiguration config) {
        this.config = config;
        this.executor = Executors.newFixedThreadPool(config.getWorkerThreads(),  r -> {
            Thread t = new Thread(r);
            t.setName("DemoHttpServerThread-" + threadCount.incrementAndGet());
            return t;
        });
        handlers = new HttpHandlers(new WebSite(config.getRootFolder()));
    }

    public CompletableFuture<Void> start() {
        return CompletableFuture.runAsync(() -> {
            log.info("Starting http server...");
            try {
                serverSocket = new ServerSocket(config.getServerPort());

                log.info ("Listening on port " + config.getServerPort() + " ...\n");

                while (active.get()) {
                    Socket socket = serverSocket.accept();
                    log.info("New connection from {}", socket.getRemoteSocketAddress());

                    //use a thread from conn pool to handle this connection
                    CompletableFuture.runAsync(new HttpConnectionHandler(socket, handlers), executor);
                }
                log.info("Shutting down ...");
            } catch (IOException e) {
                log.error("Server error", e);
            }
        });
    }

    public void stop() {
        log.info("Stopping http server...");
        active.set(false);
        executor.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            log.error("Error while closing http server...", e);
        }
        log.info("Server was shut down");
    }


}
