package com.diancu.webserver.httpapi;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that wraps the http input stream and reads the http message from it
 */
@Slf4j
public class HttpInputHandler {

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
            inputStreamReader = new InputStreamReader(new BufferedInputStream(inputStream), Charset.forName(config.getEncoding()));
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
     * Parse the request headers and decodes the headers into a map
     *
     * @return map of header name -> header value
     * @throws IOException thrown when an input error occurs
     * @throws InvalidHeadersException if the header format is incorrect
     */
    private synchronized Map<String, String> getHeaders() throws IOException {
        StatusLine statusLine = getStatusLine();
        HeaderValidator headerValidator = statusLine.getProtocol().isVer1_1()
                ? new HeaderValidator1_1() : new HeaderValidator() { /* no validation implemented for other protocols*/};
        if (httpHeaders == null) {
            httpHeaders = new HashMap<>();
            String line = nextLine();
            int separatorIndex;
            String headerName;
            String headerValue;
            while (line.length() > 0) {
                separatorIndex = line.indexOf(':');
                if (separatorIndex < 0) {
                    throw new InvalidHeadersException("Bad header line: [" + line + "]");
                } else {
                    headerName = line.substring(0, separatorIndex).toLowerCase();
                    headerValue = line.substring(separatorIndex + 1).trim();
                    if (headerValidator.isValid(headerName, headerValue)) {
                        httpHeaders.put(headerName.toLowerCase(), headerValue);
                    }
                }
                line = nextLine();
            }
            //validate for missing headers
            headerValidator.validateHeaders(httpHeaders);
        }
        return httpHeaders;

    }

    /**
     * HOST header considerations are per https://tools.ietf.org/html/rfc7230#section-5.4
     * A serverapp MUST respond with a 400 (Bad Request) status code to any
     * HTTP/1.1 request message that lacks a Host header field and to any
     * request message that contains more than one Host header field or a
     * Host header field with an invalid field-value.
     */
    class HeaderValidator1_1 implements HeaderValidator {
        public boolean isValid(String headerName, String headerValue) {
            if (HttpHeaders.HOST.equalsIgnoreCase(headerName)) {
                if (httpHeaders.containsKey(HttpHeaders.HOST.toLowerCase())) {
                    throw new InvalidHeadersException("Multiple HOST header found");
                }
            }
            return true;
        }

        @Override
        public void validateHeaders(Map<String, String> httpHeaders) {
            if (!httpHeaders.containsKey(HttpHeaders.HOST.toLowerCase())) {
                throw new InvalidHeadersException("HOST header is missing");
            }
        }
    }

    /**
     * Reads the next line from the stream. For compatibility with both Linux and Windows it reads until '\n' char is reached
     * If the '\r' char is preceding the '\n', it si discarded. It does this because during tests on Linux, the browser used '\r' '\n\'
     * as line terminator even if only '\n' was expected.
     *
     * Note: this implementation was not tested on MacOS, so errors might occur
     *
     * @return the next line from the input stream
     * @throws IOException thrown when an error occur while reading the input stream
     */
    private String nextLine() throws IOException {
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(buff);
        int nextChar  = inputStreamReader.read();
        int lineLength = 0;
        while (nextChar > 0 && (nextChar != '\n')) {
            lineLength++;
            if (lineLength > config.getMaxHeaderLineLength()) {
                throw new InvalidStatusLineException("Header line exceeded " + config.getMaxHeaderLineLength());
            }
            if  (nextChar != '\r') {
                pw.write(nextChar);
            }
            nextChar  = inputStreamReader.read();
        }
        pw.close();
        String nextLine = buff.toString(config.getEncoding());
        log.debug("nextLine: {}", nextLine);
        return nextLine;
    }

    /**
     * Writes the body part of the HTTP request from the input stream to the specified outputStream, e.g a file output stream
     * @param outputStream output stream where to write the file
     * @throws IOException an error occurred while reading or writing a stream
     */
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
