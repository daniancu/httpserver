package com.diancu.httpserver.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class HttpConnectionHandler implements Runnable {
    private final HttpInputHandler httpReader;
    private final HttpOutputHandler outputHandler;

    private final HttpHandlers handlers;

    public HttpConnectionHandler(HttpInputHandler httpReader, HttpOutputHandler outputHandler , HttpHandlers handlers) {
        this.httpReader = httpReader;
        this.outputHandler = outputHandler;
        this.handlers = handlers;
    }

    @Override
    public void run() {
        log.info("Handling new connection...");

        try {
            StatusLine statusLine = httpReader.getStatusLine();

            HttpRequestHandler handler = handlers.getHandler(statusLine.getMethod());

            if (handler != null) {
                handler.handle(httpReader, outputHandler);
            } else {
                //this method is not supported yet
                log.debug("Method {} not supported", statusLine.getMethod());
                outputHandler.writeStatusNotImplemented().writeNewLine().flush();
            }
        } catch (IOException e) {
            log.error("Connection error", e);
        } catch (InvalidStatusLineException | InvalidHeaderException e) {
            //bad request
            outputHandler.writeStatusBadRequest().writeNewLine().flush();
        } catch (RuntimeException e) {
            log.error("Error processing http request", e);
            outputHandler.writeStatusInternalError().writeNewLine().flush();
        }
    }
}
