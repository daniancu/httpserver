package com.diancu.webserver.http;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpInputHandler  {

    private final InputStreamReader inputStreamReader;
    private final HttpConfiguration config;
    //cached values
    private StatusLine statusLine;
    private Map<String, String> httpHeaders;


    public HttpInputHandler(InputStream inputStream, HttpConfiguration config) {
        this.config = config;
        if (inputStream instanceof BufferedInputStream) {
            inputStreamReader = new InputStreamReader(inputStream);
        } else {
            inputStreamReader = new InputStreamReader(new BufferedInputStream(inputStream));
        }
    }

    public synchronized StatusLine getStatusLine() throws IOException, InvalidStatusLineException {
        if (statusLine == null) {
            log.debug("Reading status line...");
            String line = nextLine();
            log.debug("status line: {}", line);
            statusLine = new StatusLine(line);
            log.debug("statusLine='{}'", statusLine);
        }
        return statusLine;
    }

    private synchronized Map<String, String> getHeaders() throws IOException {
        getStatusLine();
        if (httpHeaders == null) {
            httpHeaders = new HashMap<>();
            String line = nextLine();
            int separatorIndex = 0;
            while (!"\r".equals(line)) {
                separatorIndex = line.indexOf(':');
                if (separatorIndex < 0) {
                    throw new InvalidHeaderException(line);
                }
                else {
                    httpHeaders.put(line.substring(0, separatorIndex).toLowerCase(), line.substring(separatorIndex+1).trim());
                }
                line = nextLine();
            }
        }
        return httpHeaders;
    }


    private String nextLine() throws IOException {
        ByteArrayOutputStream buff = new ByteArrayOutputStream();

        int nextChar  = inputStreamReader.read();
        int lineLength = 0;
        while (nextChar > 0 && nextChar != '\n') {
            lineLength++;
            if (lineLength > config.getMaxStatusLineLength()) {
                throw new InvalidStatusLineException("Status line exceeded " + config.getMaxStatusLineLength());
            }
            buff.write(nextChar);
            nextChar  = inputStreamReader.read();
        }
        String nextLine = buff.toString();
        log.debug("nextLine: {}", nextLine);
        return nextLine;
    }

    public void writeRequestBody(OutputStream outputStream) throws IOException {
        log.debug("Writing body...");
        String contentLength = getHeader(HttpHeaders.CONTENT_LENGTH);
        log.debug("contentLength: {}", contentLength);
        int remaining = Integer.parseInt(contentLength);
        ByteArrayOutputStream buffStream = new ByteArrayOutputStream();
        while (remaining > 0) {
            buffStream.write(inputStreamReader.read());
            remaining--;
        }
        buffStream.writeTo(outputStream);
        buffStream.close();
    }

    void debugToOutput() throws IOException {
        log.debug("Http request ----");
        System.out.println(getStatusLine());
        System.out.println(getHeaders());
        writeRequestBody(System.out);
        log.debug("End request ----");
    }

    public String getHeader(String name) throws IOException {
        return getHeaders().get(name.toLowerCase());
    }
}