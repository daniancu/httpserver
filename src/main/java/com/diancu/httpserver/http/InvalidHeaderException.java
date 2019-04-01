package com.diancu.httpserver.http;

public class InvalidHeaderException extends RuntimeException {
    public InvalidHeaderException(String line) {
        super(line);
    }
}
