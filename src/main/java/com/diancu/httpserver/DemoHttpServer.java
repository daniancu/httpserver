package com.diancu.httpserver;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class DemoHttpServer {
    private final ServerConfiguration config;
    private Executor executor;
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
                    CompletableFuture.runAsync(() -> handleNewConnection(socket), executor);
                }
                log.info("Shutting down ...");
            } catch (IOException e) {
                log.error("server error", e);
            }
        });
    }

    private void handleNewConnection(Socket socket)  {
        PrintWriter out = null;
        try {
            out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String[] methodAndArguments = getMethodAndArguments(in.readLine());
            if ( methodAndArguments.length == 3) {
                HttpMethodHandler handler = getMethodHandler(methodAndArguments[0]);
                //handle request depending on HTTP method
                handler.handle(methodAndArguments, in, out);
            }
            out.write(StatusCodes.BAD_REQUEST);

        } catch (IOException e) {
            log.error("error handling new connection", e);
            if (out != null) {
                out.write(StatusCodes.INTERNAL_ERROR);
            }
        }
    }

    private HttpMethodHandler getMethodHandler(String method) {
        if ("GET".equals(method)) {
            return new GetMethodHandler();
        }
        throw new UnsupportedMethodException(method);
    }

    private String[] getMethodAndArguments(String firstLine) {
        Objects.requireNonNull(firstLine);
        return firstLine.split("\\s");
    }

    public void stop() {
        log.info("Server stop received");
        active.set(false);
    }
}
