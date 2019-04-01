package com.diancu.webserver.http;

import com.diancu.webserver.server.ServerConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class HttpInputHandlerTest {


    private ServerConfiguration config;

    @Before
    public void setUp() throws Exception {
        config = new ServerConfiguration(null);
    }

    @Test
    public void testWhenMessageISCorrect_thenAllFieldsAreParsedSuccessfully() throws IOException {
        String bodyText = "Some body text";
        int bodyLength = bodyText.getBytes().length;
        ByteArrayOutputStream message = new ByteArrayOutputStream();
        PrintWriter sw = new PrintWriter(message);
        sw.println("PUT /test.html HTTP/1.1");
        sw.println("Content-type: text/html");
        sw.println("Content-length: " + bodyLength);
        sw.println();
        sw.println(bodyText);
        sw.close();
        System.out.println(message);

        HttpInputHandler httpInputHandler = new HttpInputHandler(new ByteArrayInputStream(message.toByteArray()), config);
        Assert.assertEquals("PUT", httpInputHandler.getStatusLine().getMethod());
        Assert.assertEquals("/test.html", httpInputHandler.getStatusLine().getResourceUri());
        Assert.assertEquals("HTTP/1.1", httpInputHandler.getStatusLine().getProtocol());
        Assert.assertEquals("text/html", httpInputHandler.getHeader("content-type"));
        Assert.assertEquals(String.valueOf(bodyLength), httpInputHandler.getHeader("content-length"));
        ByteArrayOutputStream bodyOut = new ByteArrayOutputStream();
        httpInputHandler.writeRequestBody(bodyOut);
        Assert.assertEquals(bodyText, bodyOut.toString());
    }

    @Test (expected = InvalidStatusLineException.class)
    public void testWhenStatusLineHasIncorrectNumberOfFields_thenParserFails() throws IOException {
        ByteArrayOutputStream message = new ByteArrayOutputStream();
        PrintWriter sw = new PrintWriter(message);
        sw.println("GET / test.html HTTP/1.1");
        sw.println("Content-type: text/html");
        sw.println();
        sw.close();
        HttpInputHandler httpInputHandler = new HttpInputHandler(new ByteArrayInputStream(message.toByteArray()), config);
        httpInputHandler.getStatusLine().getMethod();
    }

    @Test (expected = InvalidStatusLineException.class)
    public void testWhenStatusStartsWithSpace_thenParserFails() throws IOException {
        ByteArrayOutputStream message = new ByteArrayOutputStream();
        PrintWriter sw = new PrintWriter(message);
        sw.println(" GET /test.html HTTP/1.1");
        sw.println();
        sw.close();
        HttpInputHandler httpInputHandler = new HttpInputHandler(new ByteArrayInputStream(message.toByteArray()), config);
        httpInputHandler.getStatusLine().getMethod();
    }

    @Test (expected = InvalidStatusLineException.class)
    public void testWhenStatusExceedsMaxLength_thenParserFails() throws IOException {
        String statusLine = "GET /test.html HTTP/1.1";
        config.setMaxStatusLineLength(statusLine.length() - 1);
        ByteArrayOutputStream message = new ByteArrayOutputStream();
        PrintWriter sw = new PrintWriter(message);
        sw.println(statusLine);
        sw.println();
        sw.close();
        HttpInputHandler httpInputHandler = new HttpInputHandler(new ByteArrayInputStream(message.toByteArray()), config);
        httpInputHandler.getStatusLine().getMethod();
    }

    @Test (expected = InvalidHeaderException.class)
    public void testWhenHeaderHeaderHasNoSeparator_thenParserFails() throws IOException {
        ByteArrayOutputStream message = new ByteArrayOutputStream();
        PrintWriter sw = new PrintWriter(message);
        sw.println("GET /test.html HTTP/1.1");
        sw.println("Content-type text/html");
        sw.println();
        sw.close();
        HttpInputHandler httpInputHandler = new HttpInputHandler(new ByteArrayInputStream(message.toByteArray()), config);
        httpInputHandler.getHeader("content-type");
    }

}