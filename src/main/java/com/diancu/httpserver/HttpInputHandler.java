package com.diancu.httpserver;

import java.io.*;

public class HttpInputHandler implements Closeable {


    private final InputStream inputStream;
    private BufferedReader inReader;
    private StatusLine statusLine;

    public HttpInputHandler(InputStream inputStream) {
        this.inputStream = inputStream;
        inReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public synchronized StatusLine readStatusLine() throws IOException, InvalidStatusLineException {
        if (statusLine != null) {
            statusLine = new StatusLine(inReader.readLine());
            return statusLine;
        }
        throw new IllegalStateException("Status line already readStatusLine");
    }

    @Override
    public void close() throws IOException {
        inReader.close();
    }

    public HttpHeaders readHeaders() {
        return null;
    }
}
