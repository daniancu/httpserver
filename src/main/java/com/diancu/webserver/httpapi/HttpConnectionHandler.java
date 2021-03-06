package com.diancu.webserver.httpapi;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Component that handles the HTTP input flow and generates the output response.
 * It uses a registry of handlers and delegates the processing to a specific handler based on the HTTP method from status line
 */
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
        //reads the status line and delegates the input processing to the handler registered for that method
        try {
            StatusLine statusLine = httpReader.getStatusLine();

            HttpRequestHandler handler = handlers.getHandler(statusLine.getMethod());

            if (handler != null) {
                handler.handle(httpReader, outputHandler);
            } else {
                //this method is not supported yet
                log.debug("Method '{}' not supported", statusLine.getMethod());
                outputHandler.writeStatusNotImplemented().writeNewLine().flush();
            }
        } catch (IOException e) {
            log.error("Connection error", e);
        } catch (InvalidStatusLineException | InvalidHeadersException e) {
            //bad request
            log.error("Invalid status or header", e);
            outputHandler.writeStatusBadRequest().writeNewLine().flush();
        } catch (RuntimeException e) {
            //handle any unhandled exception here
            log.error("Error processing http request", e);
            outputHandler.writeStatusInternalError().writeNewLine().flush();
        }
    }
}
