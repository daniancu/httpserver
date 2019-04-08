package com.diancu.webserver.httpapi;

import java.util.Map;

/**
 * Defines header validation. It has different implementations depending on the HTTP protocol version
 */
public interface HeaderValidator {

    default boolean isValid(String headerName, String headerValue) {
        return true;
    }

    default void validateHeaders(Map<String, String> httpHeaders) {

    }
}
