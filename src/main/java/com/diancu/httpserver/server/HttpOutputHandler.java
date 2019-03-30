package com.diancu.httpserver.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

public class HttpOutputHandler {
    private final PrintWriter writer;
    private final OutputStream outputStream;

    public HttpOutputHandler(OutputStream outputStream) {
        this.writer = new PrintWriter(outputStream);
        this.outputStream = outputStream;
    }


    public HttpOutputHandler writeStatusBadRequest() {
        writer.println("HTTP/1.1 400 Bad Request");
        return this;
    }

    public HttpOutputHandler writeStatusInternalError() {
        writer.print("HTTP/1.1 500 Internal Error");
        return this;
    }

    public HttpOutputHandler writeStatusOk() {
        writer.println("HTTP/1.1 200 OK");
        return this;
    }
    public HttpOutputHandler writeNewLine() {
        writer.println();
        return this;
    }

    void flush() {
        writer.flush();
    }

    public HttpOutputHandler writeStatusNotFound() {
        writer.println("HTTP/1.1 404 Not Found");
        return this;
    }

    public HttpOutputHandler writeStatusNotImplemented() {
        writer.println("HTTP/1.1 501 Not Implemented");
        return this;
    }


    public void writeHeader(String headerName, String headerValue) {
        writer.print(headerName);
        writer.print(": ");
        writer.println(headerValue);
    }

    public void writeHeaderServerAndDate() {
        writeHeader("Server", "HttpDemoServer 1.0");
        writeHeader("Date", String.valueOf(new Date()));
    }

    public void writeFromPath(Path path) throws IOException {
        Files.copy(path, outputStream);
    }
}