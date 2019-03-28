package com.diancu.httpserver;

import java.io.BufferedReader;
import java.io.PrintWriter;

public interface HttpMethodHandler {
    void handle(StatusLine firstLine, BufferedReader in, PrintWriter out);

    void handle(StatusLine statusLine, HttpHeaders headers, HttpOutputHandler outputHandler);
}
