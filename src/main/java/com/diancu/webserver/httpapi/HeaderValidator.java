package com.diancu.webserver.httpapi;

import java.util.Map;

public interface HeaderValidator {

    default boolean isValid(String headerName, String headerValue) {
        return true;
    }

    default void validateHeaders(Map<String, String> httpHeaders) {

    }
}
