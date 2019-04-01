package com.diancu.httpserver.server.http;

public class InvalidHeaderException extends RuntimeException {
    public InvalidHeaderException(String line) {
        super(line);
    }
}
