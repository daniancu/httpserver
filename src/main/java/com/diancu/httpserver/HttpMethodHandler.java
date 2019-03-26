package com.diancu.httpserver;

import java.io.BufferedReader;
import java.io.PrintWriter;

public interface HttpMethodHandler {
    void handle(String[] firstLine, BufferedReader in, PrintWriter out);
}
