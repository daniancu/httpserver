package com.diancu.webserver.websiteapi;

public class WebsiteException extends Exception {

    public WebsiteException(String message) {
        super(message);
    }

    public WebsiteException(String message, Exception cause) {
        super(message, cause);
    }
}
