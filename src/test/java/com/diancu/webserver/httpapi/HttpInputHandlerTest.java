package com.diancu.webserver.httpapi;

import com.diancu.webserver.serverapp.ServerConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

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
        sw.println("HOST: localhost");
        sw.println("Content-type: text/html");
        sw.println("Content-length: " + bodyLength);
        sw.println();
        sw.println(bodyText);
        sw.close();
        System.out.println(message);

        HttpInputHandler httpInputHandler = new HttpInputHandler(new ByteArrayInputStream(message.toByteArray()), config);
        Assert.assertEquals("PUT", httpInputHandler.getStatusLine().getMethod());
        Assert.assertEquals("/test.html", httpInputHandler.getStatusLine().getResourceUri());
        Assert.assertEquals("HTTP/1.1", httpInputHandler.getStatusLine().getProtocol().asString());
        Assert.assertEquals("text/html", httpInputHandler.getHeader("content-type"));
        Assert.assertEquals(String.valueOf(bodyLength), httpInputHandler.getHeader("content-length"));
        ByteArrayOutputStream bodyOut = new ByteArrayOutputStream();
        httpInputHandler.writeRequestBody(bodyOut);
        Assert.assertEquals(bodyText, bodyOut.toString(config.getEncoding()));
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
        config.setMaxHeaderLineLength(statusLine.length() - 1);
        ByteArrayOutputStream message = new ByteArrayOutputStream();
        PrintWriter sw = new PrintWriter(message);
        sw.println(statusLine);
        sw.println();
        sw.close();
        HttpInputHandler httpInputHandler = new HttpInputHandler(new ByteArrayInputStream(message.toByteArray()), config);
        httpInputHandler.getStatusLine().getMethod();
    }

    @Test (expected = InvalidHeadersException.class)
    public void testWhenHeaderHasNoSeparator_thenParserFails() throws IOException {
        ByteArrayOutputStream message = new ByteArrayOutputStream();
        PrintWriter sw = new PrintWriter(message);
        sw.println("GET /test.html HTTP/1.1");
        sw.println("Content-type text/html");
        sw.println();
        sw.close();
        HttpInputHandler httpInputHandler = new HttpInputHandler(new ByteArrayInputStream(message.toByteArray()), config);
        httpInputHandler.getHeader("content-type");
    }

    @Test (expected = InvalidHeadersException.class)
    public void testWhenTwoHOSTHeaders_thenParserFails() throws IOException {
        ByteArrayOutputStream message = new ByteArrayOutputStream();
        PrintWriter sw = new PrintWriter(message);
        sw.println("GET /test.html HTTP/1.1");
        sw.println("HOST: alfa");
        sw.println("HOST: beta");
        sw.println();
        sw.close();
        HttpInputHandler httpInputHandler = new HttpInputHandler(new ByteArrayInputStream(message.toByteArray()), config);
        httpInputHandler.getHeader("host");
    }

    @Test (expected = InvalidStatusLineException.class)
    public void testWhenProtocolIsBad_thenParserFails() throws IOException {
        ByteArrayOutputStream message = new ByteArrayOutputStream();
        PrintWriter sw = new PrintWriter(message);
        sw.println("GET /test.html HTTR/1.1");
        sw.println();
        sw.close();
        HttpInputHandler httpInputHandler = new HttpInputHandler(new ByteArrayInputStream(message.toByteArray()), config);
        httpInputHandler.getStatusLine();
    }

    @Test (expected = InvalidStatusLineException.class)
    public void testWhenBadProtocolVersion_thenParserFails() throws IOException {
        ByteArrayOutputStream message = new ByteArrayOutputStream();
        PrintWriter sw = new PrintWriter(message);
        sw.println("GET /test.html HTTP/11");
        sw.println();
        sw.close();
        HttpInputHandler httpInputHandler = new HttpInputHandler(new ByteArrayInputStream(message.toByteArray()), config);
        httpInputHandler.getStatusLine();
    }

}