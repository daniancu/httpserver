package com.diancu.httpserver;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class GetRequestHandler implements HttpRequestHandler {
    private final WebSite webSite;
    private final boolean sendBody;

    public GetRequestHandler(WebSite webSite) {
        this (webSite, true);
    }

    public GetRequestHandler(WebSite webSite, boolean sendBody) {
        this.webSite = webSite;
        this.sendBody = sendBody;
    }

    @Override
    public void handle(StatusLine statusLine, HttpHeaders headers, HttpOutputHandler outputHandler) throws IOException {
        log.debug("Handling GET request: {}", statusLine);

        WebResource resource = webSite.locate(statusLine.getResource());
        if (resource != null) {
            outputHandler.writeStatusOk();
            outputHandler.writeHeaderServerAndDate();
            outputHandler.writeHeader(HttpHeaders.CONTENT_TYPE, resource.getContentType());
            outputHandler.writeHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.getSize()));
            outputHandler.writeNewLine().flush();
            if (sendBody) {
                try {
                    outputHandler.writeFromPath(resource.getPath());
                } finally {
                    outputHandler.flush();
                }
            }
        } else {
            log.info("Resource not found: {}", statusLine.getResource());
            outputHandler.writeStatusNotFound().writeNewLine().flush();
        }


    }
}