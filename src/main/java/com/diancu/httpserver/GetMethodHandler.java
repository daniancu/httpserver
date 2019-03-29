package com.diancu.httpserver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetMethodHandler implements HttpMethodHandler {

   @Override
    public void handle(StatusLine statusLine, HttpHeaders headers, HttpOutputHandler outputHandler) {
        log.debug("handling GET request: {}", statusLine);

        outputHandler.writeStatus(StatusCodes.OK);
    }
}
