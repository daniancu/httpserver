package com.diancu.webserver.httpapi;

/**
 * Exception thrown when status line parsing fails
 */
public class InvalidStatusLineException extends RuntimeException {

    public InvalidStatusLineException(String line) {
        super(line);
    }
}
