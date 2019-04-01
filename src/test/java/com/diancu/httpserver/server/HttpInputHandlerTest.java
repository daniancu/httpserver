package com.diancu.httpserver.server;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class HttpInputHandlerTest {


    @Test
    public void test() throws IOException {
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

        HttpInputHandler httpInputHandler = new HttpInputHandler(new ByteArrayInputStream(message.toByteArray()));
        Assert.assertEquals("PUT", httpInputHandler.getStatusLine().getMethod());
        Assert.assertEquals("/test.html", httpInputHandler.getStatusLine().getResourceUri());
        Assert.assertEquals("HTTP/1.1", httpInputHandler.getStatusLine().getProtocol());
        Assert.assertEquals("text/html", httpInputHandler.getHeader("content-type"));
        Assert.assertEquals(String.valueOf(bodyLength), httpInputHandler.getHeader("content-length"));
        ByteArrayOutputStream bodyOut = new ByteArrayOutputStream();
        httpInputHandler.writeRequestBody(bodyOut);
        Assert.assertEquals(bodyText, bodyOut.toString());

    }

}