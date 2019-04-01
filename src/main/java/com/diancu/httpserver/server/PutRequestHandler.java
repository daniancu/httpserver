package com.diancu.httpserver.server;

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

        File tmpFile =  File.createTempFile(resourceUri, "");

        FileOutputStream outputStream = new FileOutputStream(tmpFile);
        inputHandler.writeRequestBody(outputStream);
        outputStream.close();

        if (webSite.create(resourceUri, tmpFile)) {
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
