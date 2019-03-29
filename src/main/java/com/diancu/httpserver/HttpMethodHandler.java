package com.diancu.httpserver;

public interface HttpMethodHandler {

    void handle(StatusLine statusLine, HttpHeaders headers, HttpOutputHandler outputHandler);
}
