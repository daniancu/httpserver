package com.diancu.httpserver;

import java.util.HashMap;
import java.util.Map;

public class HttpHandlers {
    private Map<String, HttpMethodHandler> methodHandlers;

    public HttpHandlers(WebResourceManager webResourceManager) {
        this.methodHandlers = new HashMap<>();
        methodHandlers.put("GET", new GetMethodHandler(webResourceManager));
    }

    public HttpMethodHandler getHandler(String method) {
        return methodHandlers.get(method);
    }
}
