package com.diancu.httpserver.server;

public class InvalidHeaderException extends RuntimeException {
    public InvalidHeaderException(String line) {
        super(line);
    }
}
