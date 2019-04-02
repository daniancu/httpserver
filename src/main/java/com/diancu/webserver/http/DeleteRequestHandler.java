package com.diancu.webserver.http;

import com.diancu.webserver.website.WebSite;
import lombok.extern.slf4j.Slf4j;

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
                    .writeCommonHeaders()
                    .writeNewLine()
                    .flush();
        } else {
            outputHandler.writeStatusInternalError()
                    .writeCommonHeaders()
                    .writeNewLine()
                    .flush();
        }

    }

}
