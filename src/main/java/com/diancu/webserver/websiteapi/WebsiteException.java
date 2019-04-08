package com.diancu.webserver.websiteapi;

/**
 * Exception thrown when abnormal situation occurs in website functionality
 */
public class WebsiteException extends Exception {

    public WebsiteException(String message) {
        super(message);
    }

    public WebsiteException(String message, Exception cause) {
        super(message, cause);
    }
}
