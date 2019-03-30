package com.diancu.httpserver.server;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Map;

@Slf4j
public class HttpInputHandler  {
    private final InputStream inputStream;
    private final BufferedReader inReader;
    //cached values
    private StatusLine statusLine;
    private Map<String, String> httpHeaders;

    public HttpInputHandler(InputStream inputStream) {
        inReader = new BufferedReader(new InputStreamReader(inputStream));
        this.inputStream = inputStream;
    }

    public synchronized StatusLine getStatusLine() throws IOException, InvalidStatusLineException {
        if (statusLine == null) {
            log.debug("Reading status line...");
            statusLine = new StatusLine(inReader.readLine());
            log.debug("statusLine='{}'", statusLine);
        }
        return statusLine;
    }

    public synchronized Map<String, String> getHeaders() throws IOException {
        getStatusLine();
        if (httpHeaders != null) {

        }
        return httpHeaders;
    }

    public InputStream getRequestBodyInputStream() throws IOException {
        getStatusLine();
        getHeaders();
        return inputStream;
    }
}
