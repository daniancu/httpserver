package com.diancu.webserver.httpapi;

/**
 *
 */
public class InvalidStatusLineException extends RuntimeException {

    public InvalidStatusLineException(String line) {
        super(line);
    }
}
