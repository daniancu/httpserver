package com.diancu.webserver.httpapi;

import java.io.IOException;

public interface HttpRequestHandler {

    void handle(HttpInputHandler inputHandler, HttpOutputHandler outputHandler) throws IOException;
}
