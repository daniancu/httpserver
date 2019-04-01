package com.diancu.webserver.http;

public class InvalidHeaderException extends RuntimeException {
    public InvalidHeaderException(String line) {
        super(line);
    }
}
