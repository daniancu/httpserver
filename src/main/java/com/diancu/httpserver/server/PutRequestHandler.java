package com.diancu.httpserver.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class PutRequestHandler implements HttpRequestHandler {
    private final WebSite webSite;

    public PutRequestHandler(WebSite webSite) {
        this.webSite = webSite;
    }

    @Override
    public void handle(HttpInputHandler inputHandler, HttpOutputHandler outputHandler) throws IOException {
        log.debug("Handling PUT request: {}", inputHandler.getStatusLine());

        String resourceUri = inputHandler.getStatusLine().getResourceUri();

        webSite.create(resourceUri, inputHandler.getRequestBodyInputStream());

        outputHandler.writeStatusCreated()
                .writeHeaderServerAndDate()
                .writeNewLine()
                .flush();

    }
}
