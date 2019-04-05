package com.diancu.webserver.httpapi;

import com.diancu.webserver.websiteapi.WebSite;
import com.diancu.webserver.websiteapi.WebsiteException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
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
        //save to a temp first
        File tmpFile =  File.createTempFile(resourceUri, "");
        FileOutputStream outputStream = new FileOutputStream(tmpFile);
        inputHandler.writeRequestBody(outputStream);
        outputStream.close();

        //add or replace file in website
        String result;
        try {
            result = webSite.addOrReplace(resourceUri, tmpFile);
            log.debug("Add or replace resulr: {}", result );

            if ("added".equalsIgnoreCase(result)) {
                outputHandler.writeStatusCreated();
            } else if ("replaced".equalsIgnoreCase(result)) {
                outputHandler.writeStatusNoContent();
            }
            outputHandler.writeCommonHeaders()
                .writeNewLine()
                .flush();
        } catch (WebsiteException e) {
            log.debug("Error while handling PUT request", e);
            outputHandler.writeStatusInternalError()
                    .writeCommonHeaders()
                    .writeNewLine()
                    .flush();
        }
    }

}
