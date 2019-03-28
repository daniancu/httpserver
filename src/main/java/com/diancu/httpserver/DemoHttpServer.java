package com.diancu.httpserver;


import lombok.extern.slf4j.Slf4j;
import java.nio.file.Files;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class DemoHttpServer {
    private final ServerConfiguration config;
    private ExecutorService executor;
    private AtomicBoolean active = new AtomicBoolean(true);

    public DemoHttpServer(ServerConfiguration config) {
        this.config = config;
        this.executor = Executors.newFixedThreadPool(config.getWorkerThreads(), r -> {
            Thread t = new Thread(r);
            t.setName("DemoHttpServerThread");
            return t;
        });

    }

    public CompletableFuture<Void> start() {
        return CompletableFuture.runAsync(() -> {
            log.info("Starting http server...");
            try {
                ServerSocket serverSocket = new ServerSocket(config.getServerPort());

                log.info ("Listening on port " + config.getServerPort() + " ...\n");

                while (active.get()) {
                    Socket socket = serverSocket.accept();
                    log.info("New connection from {}", socket.getRemoteSocketAddress().toString());
                    CompletableFuture.runAsync(new ConnectionHandler(socket), executor);
                }
                log.info("Shutting down ...");
            } catch (IOException e) {
                log.error("server error", e);
            }
        });
    }

    public void stop() {
        log.info("Server stop received");
        active.set(false);
        executor.shutdown();
    }


    public static void main(String[] args) {

        try {
            new DemoHttpServer(new ServerConfiguration(Files.createTempDirectory("demohttpserver").toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
