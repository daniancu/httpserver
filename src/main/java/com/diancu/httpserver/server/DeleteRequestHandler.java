package com.diancu.httpserver.server;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
public class DeleteRequestHandler implements HttpRequestHandler {
    private final WebSite webSite;

    public DeleteRequestHandler(WebSite webSite) {
        this.webSite = webSite;
    }

    @Override
    public void handle(HttpInputHandler inputHandler, HttpOutputHandler outputHandler) throws IOException {
        log.debug("Handling DELETE request: {}", inputHandler.getStatusLine());

        String resourceUri = inputHandler.getStatusLine().getResourceUri();


        if (webSite.delete(resourceUri)) {
            outputHandler.writeStatusCreated()
                    .writeHeaderServerAndDate()
                    .writeNewLine()
                    .flush();
        } else {
            outputHandler.writeStatusInternalError()
                    .writeHeaderServerAndDate()
                    .writeNewLine()
                    .flush();
        }

    }

}
