package com.diancu.webserver.httpapi;

import com.diancu.webserver.websiteapi.WebSite;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * https://tools.ietf.org/html/rfc7231#section-4.3.7
 */
@Slf4j
public class OptionsRequestHandler implements HttpRequestHandler {
    private final WebSite webSite;

    public OptionsRequestHandler(WebSite webSite) {
        this.webSite = webSite;
    }

    @Override
    public void handle(HttpInputHandler inputHandler, HttpOutputHandler outputHandler) throws IOException {
        log.debug("Handling DELETE request: {}", inputHandler.getStatusLine());

        String resourceUri = inputHandler.getStatusLine().getResourceUri();
        if ("/*".equalsIgnoreCase(resourceUri) || webSite.locate(resourceUri) != null) {
            outputHandler.writeStatusOk()
                    .writeCommonHeaders()
                    .writeHeader(HttpHeaders.ALLOW, "GET,HEAD,PUT,DELETE,OPTIONS")
                    .writeHeader(HttpHeaders.CONTENT_LENGTH, "0")
                    .writeNewLine()
                    .flush();
        } else {
            outputHandler.writeStatusNotFound()
                    .writeCommonHeaders()
                    .writeNewLine()
                    .flush();
        }
    }
}
