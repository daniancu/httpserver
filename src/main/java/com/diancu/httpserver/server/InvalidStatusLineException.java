package com.diancu.httpserver.server;

/**
 *
 */
public class InvalidStatusLineException extends Throwable {

    public InvalidStatusLineException(String line) {
        super(line);
    }
}
