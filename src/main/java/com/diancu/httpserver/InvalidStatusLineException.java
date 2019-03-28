package com.diancu.httpserver;

/**
 *
 */
public class InvalidStatusLineException extends Throwable {

    public InvalidStatusLineException(String line) {
        super(line);
    }
}
