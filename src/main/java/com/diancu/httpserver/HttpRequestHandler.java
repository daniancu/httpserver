package com.diancu.httpserver;

import java.io.IOException;

public interface HttpRequestHandler {

    void handle(StatusLine statusLine, HttpHeaders headers, HttpOutputHandler outputHandler) throws IOException;
}
