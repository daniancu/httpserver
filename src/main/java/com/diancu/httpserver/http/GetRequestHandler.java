package com.diancu.httpserver.http;

import com.diancu.httpserver.website.WebResource;
import com.diancu.httpserver.website.WebSite;
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
    public void handle(HttpInputHandler inputHandler, HttpOutputHandler outputHandler) throws IOException {
        log.debug("Handling GET request: {}", inputHandler.getStatusLine());

        WebResource resource = webSite.locate(inputHandler.getStatusLine().getResourceUri());
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
            log.info("Resource not found: {}", inputHandler.getStatusLine().getResourceUri());
            outputHandler.writeStatusNotFound().writeNewLine().flush();
        }


    }
}
