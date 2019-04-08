package com.diancu.webserver.httpapi;

import com.diancu.webserver.websiteapi.WebResourceNotFoundException;
import com.diancu.webserver.websiteapi.WebSite;
import com.diancu.webserver.websiteapi.WebsiteException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * HTTP DELETE implementation
 */
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

        try {
            webSite.delete(resourceUri);
            outputHandler.writeStatusNoContent()
                    .writeCommonHeaders()
                    .writeNewLine()
                    .flush();
        } catch (WebResourceNotFoundException e) {
            outputHandler.writeStatusNotFound()
                    .writeCommonHeaders()
                    .writeNewLine()
                    .flush();
        } catch (WebsiteException e) {
            outputHandler.writeStatusInternalError()
                    .writeCommonHeaders()
                    .writeNewLine()
                    .flush();
        }

    }

}
