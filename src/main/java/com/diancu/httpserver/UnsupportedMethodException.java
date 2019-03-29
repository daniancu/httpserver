package com.diancu.httpserver;

public class UnsupportedMethodException extends Exception {

    public UnsupportedMethodException(String method) {
        super(method);
    }
}
