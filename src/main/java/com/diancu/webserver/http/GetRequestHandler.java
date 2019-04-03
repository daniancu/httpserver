package com.diancu.webserver.http;

import com.diancu.webserver.website.WebResource;
import com.diancu.webserver.website.WebSite;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

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
        log.debug("Handling GET request: {}", inputHandler.getStatusLine());

        WebResource resource = webSite.locate(inputHandler.getStatusLine().getResourceUri());
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
            log.info("Resource not found: {}", inputHandler.getStatusLine().getResourceUri());
            outputHandler.writeStatusNotFound().writeNewLine().flush();
        }


    }
}
