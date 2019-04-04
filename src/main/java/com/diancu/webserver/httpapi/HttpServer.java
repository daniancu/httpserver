package com.diancu.webserver.httpapi;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class HttpServer {
    private final HttpConfiguration config;
    private final HttpHandlers handlers;
    private final ExecutorService executor;
    private AtomicBoolean active = new AtomicBoolean(true);
    private ServerSocket serverSocket;

    public HttpServer(HttpConfiguration config, HttpHandlers handlers, ExecutorService executor) {
        this.config = config;
        this.handlers = handlers;
        this.executor = executor;
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
                    socket.setSoTimeout(config.getSoTimeout());
                    HttpInputHandler httpInputHandler = new HttpInputHandler(socket.getInputStream(), config);
                    HttpOutputHandler httpOutputHandler = new HttpOutputHandler(socket.getOutputStream());
                    HttpConnectionHandler runnable = new HttpConnectionHandler(httpInputHandler, httpOutputHandler, handlers);
                    //use a thread from conn pool to handle this connection
                    CompletableFuture.runAsync(runnable, executor).thenRun( () -> closeSocket(socket));
                }
                log.info("Shutting down ...");
            } catch (IOException e) {
                log.error("Server error", e);
            }
        });
    }

    private void closeSocket(Socket socket) {
        log.debug("Closing socket to {}...", socket.getRemoteSocketAddress());
        try {
            socket.close();
        } catch (IOException e) {
            log.error("Could not close socket", e);
        }
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
