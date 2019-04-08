package com.diancu.webserver.httpapi;

/**
 * Exception thrown when http header parsing fails
  */
public class InvalidHeadersException extends RuntimeException {
    public InvalidHeadersException(String line) {
        super(line);
    }
}
