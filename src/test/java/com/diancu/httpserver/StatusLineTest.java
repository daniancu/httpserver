package com.diancu.httpserver;

import org.junit.Assert;

import static org.junit.jupiter.api.Assertions.*;

class StatusLineTest {


    public void testStatusLine() throws InvalidStatusLineException {
        StatusLine statusLine = new StatusLine("GET /test.html HTTP/1.1");

        Assert.assertEquals("GET", statusLine.getMethod());
        Assert.assertEquals("/test.html", statusLine.getResource());
        Assert.assertEquals("HTTP/1.1", statusLine.getProtocol());
    }
}