package com.diancu.webserver.httpapi;

public class InvalidHeadersException extends RuntimeException {
    public InvalidHeadersException(String line) {
        super(line);
    }
}
