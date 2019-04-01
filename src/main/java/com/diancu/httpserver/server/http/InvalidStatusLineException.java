package com.diancu.httpserver.server.http;

/**
 *
 */
public class InvalidStatusLineException extends RuntimeException {

    public InvalidStatusLineException(String line) {
        super(line);
    }
}
