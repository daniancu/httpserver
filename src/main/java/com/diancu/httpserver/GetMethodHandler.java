package com.diancu.httpserver;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class GetMethodHandler implements HttpMethodHandler {
    private WebResourceManager webResourceManager;

    public GetMethodHandler(WebResourceManager webResourceManager) {
        this.webResourceManager = webResourceManager;
    }

    @Override
    public void handle(StatusLine statusLine, HttpHeaders headers, HttpOutputHandler outputHandler) {
        log.debug("Handling GET request: {}", statusLine);

        WebResource resource = webResourceManager.locate(statusLine.getResource());
        if (resource != null) {
            outputHandler.writeStatusOk();
            outputHandler.writeHeader(HttpHeaders.CONTENT_TYPE, resource.getContentType());
            outputHandler.writeHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.getSize()));
            outputHandler.writeNewLine().flush();
            try {
                outputHandler.writeFromPath(resource.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                outputHandler.flush();
            }
        } else {
            log.info("Resource not found: {}", statusLine.getResource());
            outputHandler.writeStatusNotFound().writeNewLine().flush();
        }


    }
}
