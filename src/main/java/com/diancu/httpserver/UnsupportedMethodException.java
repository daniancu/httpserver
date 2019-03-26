package com.diancu.httpserver;

public class UnsupportedMethodException extends RuntimeException {

    public UnsupportedMethodException(String method) {
        super(method);
    }
}
