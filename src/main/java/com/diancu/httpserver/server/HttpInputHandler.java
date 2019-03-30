package com.diancu.httpserver.server;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
@Slf4j
public class HttpInputHandler implements Closeable {
    private BufferedReader inReader;
    private StatusLine statusLine;

    public HttpInputHandler(InputStream inputStream) {
        inReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public synchronized StatusLine readStatusLine() throws IOException, InvalidStatusLineException {
        if (statusLine == null) {
            log.debug("Reading status line...");
            statusLine = new StatusLine(inReader.readLine());
            log.debug("statusLine='{}'", statusLine);
        }
        return statusLine;
    }

    @Override
    public void close() throws IOException {
        inReader.close();
    }

    public HttpHeaders readHeaders() {
        return null;
    }
}
