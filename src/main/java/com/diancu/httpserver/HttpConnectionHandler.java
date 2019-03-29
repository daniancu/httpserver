package com.diancu.httpserver;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

@Slf4j
public class HttpConnectionHandler implements Closeable, Runnable {
    private final Socket socket;
    private final HttpHandlers handlers;

    public HttpConnectionHandler(Socket socket, HttpHandlers handlers) {
        this.socket = socket;
        this.handlers = handlers;
    }

    @Override
    public void close() throws IOException {
        log.debug("Closing connection with {}...", socket.getRemoteSocketAddress());
        socket.close();
    }

    @Override
    public void run() {
        log.info("Handling new connection...");
        HttpInputHandler httpReader;
        HttpOutputHandler outputHandler = null;

        try {
            httpReader = new HttpInputHandler(socket.getInputStream());
            outputHandler = new HttpOutputHandler(socket.getOutputStream());
            StatusLine statusLine = httpReader.readStatusLine();
            log.debug("statusLine: {}", statusLine);
            HttpHeaders headers = httpReader.readHeaders();

            HttpMethodHandler handler = handlers.getHandler(statusLine.getMethod());

            if (handler != null) {
                handler.handle(statusLine, headers, outputHandler);
            } else {
                //this method is not supported yet
                log.debug("Method {} not supported", statusLine.getMethod());
                outputHandler.writeStatusBadRequest().writeNewLine().flush();
            }
        } catch (IOException e) {
            log.error("Connection error", e);
        } catch (InvalidStatusLineException e) {
            //bad request
            outputHandler.writeStatusBadRequest().writeNewLine().flush();
        } catch (RuntimeException e) {
            log.error("Error processing http request", e);
            outputHandler.writeStatusInternalError().writeNewLine().flush();
        } finally {
            try {
                close();
            } catch (IOException e) {
                log.error("error closing connection", e);
            }
        }
    }
}
