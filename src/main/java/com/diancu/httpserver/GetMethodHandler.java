package com.diancu.httpserver;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Arrays;

@Slf4j
public class GetMethodHandler implements HttpMethodHandler {


    @Override
    public void handle(String[] firstLine, BufferedReader in, PrintWriter out) {
        log.debug("handling GET request: {}", Arrays.asList(firstLine));
        out.println("HTTP/1.1 200 OK");
        out.close();
    }
}
