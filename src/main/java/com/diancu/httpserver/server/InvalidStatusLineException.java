package com.diancu.httpserver.server;

/**
 *
 */
public class InvalidStatusLineException extends RuntimeException {

    public InvalidStatusLineException(String line) {
        super(line);
    }
}
