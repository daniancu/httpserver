package com.diancu.webserver.httpapi;

import com.diancu.webserver.websiteapi.WebSite;

import java.util.HashMap;
import java.util.Map;

public class HttpHandlers {
    private Map<String, HttpRequestHandler> methodHandlers;

    public HttpHandlers(WebSite webSite) {
        this.methodHandlers = new HashMap<>();
        methodHandlers.put("GET", new GetRequestHandler(webSite));
        methodHandlers.put("HEAD", new GetRequestHandler(webSite, false));
        methodHandlers.put("PUT", new PutRequestHandler(webSite));
        methodHandlers.put("DELETE", new DeleteRequestHandler(webSite));
        methodHandlers.put("OPTIONS", new OptionsRequestHandler(webSite));
    }

    public HttpRequestHandler getHandler(String method) {
        return methodHandlers.get(method);
    }
}
