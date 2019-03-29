package com.diancu.httpserver;

import java.util.Arrays;
import java.util.Objects;


public class StatusLine {

    private String[] fields;

    public StatusLine(String line) throws InvalidStatusLineException {
        parseLine(line);
    }

    private void parseLine(String line) throws InvalidStatusLineException {
        Objects.requireNonNull(line);
        fields = line.split("\\s");
        if (fields.length != 3) {
            throw new InvalidStatusLineException(line);
        }
    }

    public String getMethod() {
        return fields[0];
    }

    public String getResource() {
        return fields[1];
    }

    public String getProtocol() {
        return fields[2];
    }

    @Override
    public String toString() {
        return "StatusLine{" +
                "fields=" + Arrays.toString(fields) +
                '}';
    }
}
