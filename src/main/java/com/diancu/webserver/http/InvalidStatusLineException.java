package com.diancu.webserver.http;

/**
 *
 */
public class InvalidStatusLineException extends RuntimeException {

    public InvalidStatusLineException(String line) {
        super(line);
    }
}
