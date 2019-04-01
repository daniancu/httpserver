package com.diancu.httpserver.server.http;

import java.io.IOException;

public interface HttpRequestHandler {

    void handle(HttpInputHandler inputHandler, HttpOutputHandler outputHandler) throws IOException;
}
