package com.diancu.webserver.httpapi;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

/**
 * StatusLine object is created from the request header (first line sent by client)
 */
@Slf4j
public class StatusLine {

    private String[] fields;
    private HttpProtocol protocol;

    public StatusLine(String line) throws InvalidStatusLineException {
        parseLine(line);
    }

    private void parseLine(String line) throws InvalidStatusLineException {
        Objects.requireNonNull(line);
        fields = line.split("\\s");
        if (fields.length != 3) {
            throw new InvalidStatusLineException(line);
        }
        protocol = new HttpProtocol(fields[2]);
    }

    public String getMethod() {
        return fields[0];
    }

    public String getResourceUri() {
        return getResourceUri(StandardCharsets.UTF_8.name());
    }

    public String getResourceUri(String enc) {
        try {
            return URLDecoder.decode(fields[1], enc);
        } catch (UnsupportedEncodingException e) {
            log.error("decode error", e);
            return fields  [1];
        }
    }

    public HttpProtocol getProtocol() {
        return protocol;
    }

    @Override
    public String toString() {
        return "StatusLine{" +
                "fields=" + Arrays.toString(fields) +
                '}';
    }
}
