package com.diancu.httpserver.server.http;

import com.diancu.httpserver.server.website.WebSite;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class DebugRequestHandler implements HttpRequestHandler {
    private final WebSite webSite;

    public DebugRequestHandler(WebSite webSite) {
        this.webSite = webSite;
    }

    @Override
    public void handle(HttpInputHandler inputHandler, HttpOutputHandler outputHandler) throws IOException {
        log.info("Dump request");

//        inputHandler.debugToOutput();

        outputHandler.writeStatusOk().writeNewLine().flush();
    }
}
