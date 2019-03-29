package com.diancu.httpserver;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

@Slf4j
public class ConnectionHandler implements Closeable, Runnable {
    private final Socket socket;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void close() throws IOException {
        log.debug("Closing connection with {}...", socket.getRemoteSocketAddress());
        socket.close();
    }

    @Override
    public void run() {
        log.info("Handling new connection");
        HttpInputHandler httpReader;
        HttpOutputHandler outputHandler = null;

        try {
            httpReader = new HttpInputHandler(socket.getInputStream());
            outputHandler = new HttpOutputHandler(socket.getOutputStream());
            StatusLine statusLine = httpReader.readStatusLine();
            log.debug("statusLine: {}", statusLine);
            HttpHeaders headers = httpReader.readHeaders();
            if ("GET".equals(statusLine.getMethod())) {
                new GetMethodHandler().handle(statusLine, headers, outputHandler);
            } else {
                throw new UnsupportedMethodException(statusLine.getMethod());
            }
            close();
        } catch (IOException e) {
            log.error("Connection error", e);

        } catch (InvalidStatusLineException | UnsupportedMethodException e) {
            outputHandler.writeBadRequest();
        } catch (RuntimeException e) {
            log.error("", e);
            outputHandler.writeBadRequest();
        }
    }
}
