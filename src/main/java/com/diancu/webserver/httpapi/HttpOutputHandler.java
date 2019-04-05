package com.diancu.webserver.httpapi;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

@Slf4j
public class HttpOutputHandler {
    private final PrintWriter writer;
    private final OutputStream outputStream;

    public HttpOutputHandler(OutputStream outputStream) {
        this.writer = new PrintWriter(outputStream);
        this.outputStream = outputStream;
    }


    public HttpOutputHandler writeStatusBadRequest() {
        return writeStatus("HTTP/1.1 400 Bad Request");
    }

    public HttpOutputHandler writeStatusInternalError() {
        return writeStatus("HTTP/1.1 500 Internal Error");
    }

    public HttpOutputHandler writeStatusOk() {
        return writeStatus("HTTP/1.1 200 OK");
    }
    public HttpOutputHandler writeStatusNotFound() {
        return writeStatus ("HTTP/1.1 404 Not Found");
    }

    public HttpOutputHandler writeStatusNotImplemented() {
        return writeStatus("HTTP/1.1 501 Not Implemented");
    }

    public HttpOutputHandler writeStatusCreated() {
        return writeStatus("HTTP/1.1 201 Created");
    }

    public HttpOutputHandler writeStatusNoContent() {
        return writeStatus("HTTP/1.1 204 No Content");
    }

    private HttpOutputHandler writeStatus(String status) {
        log.debug("Sending status: {}", status);
        writer.println(status);
        return this;
    }

    public HttpOutputHandler writeNewLine() {
        writer.println();
        return this;
    }

    void flush() {
        writer.flush();
    }

    public HttpOutputHandler writeHeader(String headerName, String headerValue) {
        log.debug("Write header: {} -> {}", headerName, headerValue);
        writer.print(headerName);
        writer.print(": ");
        writer.println(headerValue);
        return this;
    }

    public HttpOutputHandler writeCommonHeaders() {
        writeHeader(HttpHeaders.SERVER, "HttpDemoServer 1.0");
        writeHeader(HttpHeaders.DATE, String.valueOf(new Date()));
        writeHeader(HttpHeaders.CONNECTION, "close");
        return this;
    }

    public void writeFromPath(Path path) throws IOException {
        Files.copy(path, outputStream);
    }

    public void writeFrom(InputStream inputStream) throws IOException {
        IOUtils.copy(inputStream, outputStream);
        inputStream.close();
    }
}
