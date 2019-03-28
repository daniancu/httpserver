package com.diancu.httpserver;

import java.io.*;
import java.util.Objects;

public class StatusLine {

    public StatusLine(String line) throws InvalidStatusLineException {
        parseLine(line);
    }

    private void parseLine(String line) throws InvalidStatusLineException {
        Objects.requireNonNull(line);
        String[] fields = line.split("\\s");
        if (fields.length == 3) {

        } else throw new InvalidStatusLineException(line);
    }

    public String getMethod() {
        return null;
    }

    public String getResource() {
        return null;
    }

    public String getProtocol() {
        return null;
    }
}
