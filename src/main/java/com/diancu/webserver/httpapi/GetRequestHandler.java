package com.diancu.webserver.httpapi;

import com.diancu.webserver.websiteapi.WebResource;
import com.diancu.webserver.websiteapi.WebSite;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * HTTP GET/HEAD Implementation
 */
@Slf4j
public class GetRequestHandler implements HttpRequestHandler {
    private final WebSite webSite;
    private final boolean sendBody;//true for GET, false for HEAD

    public GetRequestHandler(WebSite webSite) {
        this (webSite, true);
    }

    public GetRequestHandler(WebSite webSite, boolean sendBody) {
        this.webSite = webSite;
        this.sendBody = sendBody;
    }

    @Override
    public void handle(HttpInputHandler inputHandler, HttpOutputHandler outputHandler) throws IOException {
        StatusLine statusLine = inputHandler.getStatusLine();
        log.debug("Handling GET request: {}", statusLine);
        if (inputHandler.getHeader(HttpHeaders.HOST) == null) {
            outputHandler.writeStatusBadRequest().writeNewLine().flush();
            return;
        }
        WebResource resource = webSite.locate(statusLine.getResourceUri());
        if (resource != null) {
            InputStream inputStream = resource.getInputStream();
            outputHandler.writeStatusOk();
            outputHandler.writeCommonHeaders();
            outputHandler.writeHeader(HttpHeaders.CONTENT_TYPE, resource.getContentType());
            outputHandler.writeHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(inputStream.available()));
            outputHandler.writeNewLine().flush();
            if (sendBody) {//HEAD command does not send body
                try {
                    outputHandler.writeFrom(inputStream);
                } finally {
                    outputHandler.flush();
                }
            }
        } else {
            log.info("Resource not found: {}", statusLine.getResourceUri());
            outputHandler.writeStatusNotFound().writeNewLine().flush();
        }


    }
}
