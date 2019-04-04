package com.diancu.webserver.httpapi;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
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
            inputStreamReader = new InputStreamReader(inputStream, Charset.forName(config.getEncoding()));
        } else {
            inputStreamReader = new InputStreamReader(new BufferedInputStream(inputStream), Charset.forName(config.getEncoding()) );
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

    /**
     *    Parse the request headers
     *
     *    HOST header considerations are per https://tools.ietf.org/html/rfc7230#section-5.4
     *    A serverapp MUST respond with a 400 (Bad Request) status code to any
     *    HTTP/1.1 request message that lacks a Host header field and to any
     *    request message that contains more than one Host header field or a
     *    Host header field with an invalid field-value.
     * @return
     * @throws IOException
     */
    private synchronized Map<String, String> getHeaders() throws IOException {
        getStatusLine();
        if (httpHeaders == null) {
            httpHeaders = new HashMap<>();
            String line = nextLine();
            int separatorIndex = 0;
            while (!"\r".equals(line)) {
                separatorIndex = line.indexOf(':');
                if (separatorIndex < 0) {
                    throw new InvalidHeadersException(line);
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
        PrintWriter pw = new PrintWriter(buff);
        int nextChar  = inputStreamReader.read();
        int lineLength = 0;
        while (nextChar > 0 && nextChar != '\n') {
            lineLength++;
            if (lineLength > config.getMaxStatusLineLength()) {
                throw new InvalidStatusLineException("Status line exceeded " + config.getMaxStatusLineLength());
            }
            pw.write(nextChar);
            nextChar  = inputStreamReader.read();
        }
        pw.close();
        String nextLine = buff.toString(config.getEncoding());
        System.out.println("nextLine = " + nextLine);
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

    public String getHeader(String name) throws IOException {
        return getHeaders().get(name.toLowerCase());
    }
}