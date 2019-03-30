package com.diancu.httpserver;

import java.util.HashMap;
import java.util.Map;

public class HttpHandlers {
    private Map<String, HttpRequestHandler> methodHandlers;

    public HttpHandlers(WebSite webSite) {
        this.methodHandlers = new HashMap<>();
        methodHandlers.put("GET", new GetRequestHandler(webSite));
        methodHandlers.put("HEAD", new GetRequestHandler(webSite, false));
    }

    public HttpRequestHandler getHandler(String method) {
        return methodHandlers.get(method);
    }
}
