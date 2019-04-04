package com.diancu.webserver.httpapi;

import java.util.Objects;

/**
 * Handles protocol parsing according to
 * https://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html
 */
public class HttpProtocol {

    private final String field;
    private int majorVersion;
    private int minorVersion;

    public HttpProtocol(String field) {
        Objects.requireNonNull(field);
        this.field = field;
        if (field.toLowerCase().startsWith("http/")) {
            int dotIndex = field.indexOf('.');
            if (dotIndex > 0) {
                try {
                    majorVersion = Integer.parseInt(field.substring(5, dotIndex));
                    minorVersion = Integer.parseInt(field.substring(dotIndex + 1));
                } catch (NumberFormatException e) {
                    throw new InvalidStatusLineException("Bad protocol version: " + e.getMessage());
                }
            } else {
                throw new InvalidStatusLineException("Bad protocol, '.' found");
            }
        } else {
            throw new InvalidStatusLineException("Bad protocol field: " + field);
        }
    }

    public boolean isVer1_1() {
        return (majorVersion == 1) && (minorVersion == 1);
    }

    public String asString() {
        return field;
    }
}
