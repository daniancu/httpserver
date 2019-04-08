package com.diancu.webserver.websiteapi;

/**
 * Exception throw when a resource could not be found in the website
 */
public class WebResourceNotFoundException extends WebsiteException {

    public WebResourceNotFoundException(String message) {
        super(message);
    }
}
