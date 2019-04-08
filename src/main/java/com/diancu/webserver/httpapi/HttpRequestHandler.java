package com.diancu.webserver.httpapi;

import java.io.IOException;

/**
 * Defines a HTTP request handler. A typical handler implementation reads the HTTO protocol request
 * using a HttpInputHandler and the generates the response using a HttpOutputHandler
 */
public interface HttpRequestHandler {

    void handle(HttpInputHandler inputHandler, HttpOutputHandler outputHandler) throws IOException;
}
