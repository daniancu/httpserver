package com.diancu.httpserver;

import java.io.OutputStream;
import java.io.PrintWriter;

public class HttpOutputHandler {
    private final PrintWriter out;

    public HttpOutputHandler(OutputStream outputStream) {
        this.out = new PrintWriter(outputStream);
    }


    public void writeBadRequest() {
        out.println("HTTP/1.1 400 Bad Request");
    }

    public void writeInternalError() {
        out.println("HTTP/1.1 500 Internal Error");
    }

    public void writeStatus(int ok) {
        out.println("HTTP/1.1 200 OK");
    }
}
