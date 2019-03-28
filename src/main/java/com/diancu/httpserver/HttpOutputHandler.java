package com.diancu.httpserver;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public class HttpOutputHandler implements Closeable {
    public HttpOutputHandler(OutputStream outputStream) {

    }

    @Override
    public void close() throws IOException {

    }

    public void writeInternalServerError() {

    }

    public void writeBadRequest() {

    }
}
